package tech.seife.emily.datamanager.data.audio;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.jetbrains.annotations.Nullable;
import tech.seife.emily.datamanager.files.FileManager;
import tech.seife.emily.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class QueueManager {

    private final FileManager fileManager;
    private final Map<String, Map<String, EmbedBuilder>> queueManager;
    private final Map<String, Integer> userPage;
    private final Gson gson;

    public QueueManager(FileManager fileManager) {
        this.fileManager = fileManager;
        gson = fileManager.getGson();
        queueManager = new HashMap<>();
        userPage = new HashMap<>();
    }

    public void addReactions(Message message) {
        message.addReaction("U+274C").queue();
        message.addReaction("U+23ED").queue();
        message.addReaction("U+23EE").queue();
    }

    public void addQueueManager(String user, String id, EmbedBuilder embedBuilder) {
        Map<String, EmbedBuilder> map = new HashMap<>();

        if (queueManager.get(user) != null) {
            map = queueManager.get(user);
            queueManager.replace(user, map);
        }

        map.put(id, embedBuilder);

        queueManager.put(user, map);
    }

    public void setUserPage(String userId, int page) {
        if (userPage.containsKey(userId)) {
            userPage.replace(userId, page);
        }
        userPage.put(userId, page);
    }

    public void clearUserPage(String userId) {
        queueManager.remove(userId);
    }

    public Integer getPage(String userID) {
        return userPage.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(userID))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public EmbedBuilder getEmbed(String userId, String embedId) {
        if (queueManager.get(userId) != null && queueManager.get(userId).get(embedId) != null) {
            return queueManager.get(userId).get(embedId);
        }
        return null;
    }

    public void clearSongs() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        jsonObject.remove("currentlyPlaying");
        jsonObject.remove("queue");
        jsonObject.remove("latestId");

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));

    }

    public MultiValuedMap<String, String> getQueuedSongs(long startingPlace, long toGrab) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();

        if (jsonObject.get("queue") != null && jsonObject.get("queue").getAsJsonObject().size() >= startingPlace) {
            for (long i = startingPlace; i <= (startingPlace + toGrab); i++) {
                if (jsonObject.get("queue").getAsJsonObject().get(String.valueOf(i)) != null) {
                    map.put(jsonObject.get("queue").getAsJsonObject().get(String.valueOf(i)).getAsJsonObject().get("name").getAsString(), jsonObject.get("queue").getAsJsonObject().get(String.valueOf(i)).getAsJsonObject().get("url").getAsString());
                }
            }
        }

        return map;
    }

    public EmbedBuilder getEmbedQueue(long startingNumber, int currentPage, JDA jda, EmbedBuilder embedBuilder) {
        if (startingNumber < 0) return null;

        StringBuilder sb = new StringBuilder();


        embedBuilder.setTitle("Queue for " + jda.getSelfUser().getName());

        sb.append("Currently playing: ").append(String.format("[%s](%s)", getCurrentSongName(), getCurrentSongUrl())).append("\n\n\n\n");

        MultiValuedMap<String, String> songs = getQueuedSongs(startingNumber - 1, 9);

        for (Map.Entry<String, String> entry : songs.entries()) {
            sb.append(startingNumber + 1).append(". ").append(String.format("[%s](%s)", entry.getKey(), entry.getValue())).append("\n");
            startingNumber++;
        }

        embedBuilder.setFooter("Total songs: " + getAmountOfSongs() + " page: " + (currentPage + 1) + " / " + (int) (Math.ceil(getAmountOfSongs() / 10) + 1));
        embedBuilder.setDescription(sb);

        return embedBuilder;
    }


    public long getAmountOfSongs() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);


        return jsonObject.get("queue") != null ? jsonObject.get("queue").getAsJsonObject().keySet().size() : 0;
    }

    public boolean hasNext() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        return jsonObject.get("queue").getAsJsonObject().size() >= 1;
    }

    public void rePosition() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        JsonObject newJson = jsonObject.deepCopy();

        for (Map.Entry<String, JsonElement> entry : jsonObject.get("queue").getAsJsonObject().entrySet()) {
            newJson.get("queue").getAsJsonObject().remove(entry.getKey());

            newJson.get("queue").getAsJsonObject().add(String.valueOf(Utils.parseLong(entry.getKey()) - 1L), entry.getValue());
        }

        long id = newJson.get("latestId").getAsLong() - 1;

        newJson.remove("latestId");
        newJson.addProperty("latestId", id);

        fileManager.saveGsonSongsQueue(gson.fromJson(newJson, Map.class));
    }

    public String getCurrentSongName() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        if (jsonObject.get("currentlyPlaying") == null || jsonObject.get("currentlyPlaying").getAsJsonObject() == null || jsonObject.get("currentlyPlaying").getAsJsonObject().get("name").getAsString() == null) {
            return "Nothing is playing";
        }

        return jsonObject.get("currentlyPlaying").getAsJsonObject().get("name").getAsString();
    }


    public String getCurrentSongUrl() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        return jsonObject.get("currentlyPlaying").getAsJsonObject().get("url").getAsString();
    }

    public void deleteQueueSong(int id) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        JsonObject queue = jsonObject.get("queue").getAsJsonObject();

        if (queue.get(String.valueOf(id)) != null) {
            queue.remove(String.valueOf(id));
        }

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
    }

    public void removeCurrentSong() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        jsonObject.remove("currentlyPlaying");

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
    }

    public void pushNextSong() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        JsonObject queue = jsonObject.get("queue").getAsJsonObject();

        JsonObject song = queue.get("0").getAsJsonObject();


        addCurrentSong(song.get("name").getAsString(), song.get("url").getAsString().replace("https://www.youtube.com/watch?v=", ""), song.get("duration").getAsString());

    }

    public void addCurrentSong(String name, String id, String duration) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        jsonObject.remove("currentlyPlaying");

        JsonObject currentPlaying = getObject(name, id, duration);

        jsonObject.add("currentlyPlaying", currentPlaying);

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));

    }

    public void addToQueue(String name, String videoId, String duration) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        JsonObject newSong = getObject(name, videoId, duration);

        if (jsonObject.get("latestId") == null) {
            jsonObject.addProperty("latestId", -1);
        }

        long id = jsonObject.get("latestId").getAsLong() + 1;

        if (jsonObject.get("queue") == null) {
            jsonObject.add("queue", new JsonObject());
        }

        jsonObject.get("queue").getAsJsonObject().add(String.valueOf(id), newSong);

        jsonObject.remove("latestId");
        jsonObject.addProperty("latestId", id);

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));

    }

    private JsonObject getObject(String name, String id, String duration) {
        JsonObject object = new JsonObject();

        object.addProperty("name", name);
        object.addProperty("url", "https://www.youtube.com/watch?v=" + id);
        object.addProperty("duration", duration);

        return object;
    }
}
