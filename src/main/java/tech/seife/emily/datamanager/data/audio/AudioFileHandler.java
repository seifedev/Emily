package tech.seife.emily.datamanager.data.audio;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.jetbrains.annotations.Nullable;
import tech.seife.emily.datamanager.files.FileManager;
import tech.seife.emily.utils.Utils;

import java.util.Map;

public class AudioFileHandler implements AudioData {

    private final FileManager fileManager;
    private final Gson gson;

    public AudioFileHandler(FileManager fileManager) {
        this.fileManager = fileManager;
        gson = fileManager.getGson();
    }


    @Override
    public boolean clearSongs(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        if (jsonObject.has(serverId)) {
            jsonObject.remove(serverId);

            fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));

            return true;
        }

        return false;
    }


    @Nullable
    @Override
    public MultiValuedMap<String, Song> getQueue(String serverId, long startingPlace, long howManyToGet) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        //Check we can fetch data from the data storage.
        if (!jsonObject.has(serverId) || !jsonObject.get(serverId).getAsJsonObject().has("upComingSongs") || !jsonObject.get(serverId).getAsJsonObject().get("upComingSongs").getAsJsonObject().has(String.valueOf(startingPlace))) {
            return null;
        }

        JsonObject queue = jsonObject.get(serverId).getAsJsonObject().get("upComingSongs").getAsJsonObject();


        MultiValuedMap<String, Song> map = new ArrayListValuedHashMap<>();

        long recorded = 0;
        boolean canRecord = false;

        for (String id : queue.keySet()) {
            if (Utils.parseLong(id) == startingPlace) {
                canRecord = true;
            }

            if (canRecord) {
                JsonObject details = queue.getAsJsonObject(id);
                map.put(id, getSongFromJsonObject(details));
                recorded++;
            }

            //We want to break out of the loop
            //When we fetch all our songs
            if (recorded >= howManyToGet) {
                break;
            }
        }
        return map;
    }

    @Override
    public long getAmountOfRemainingSongs(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        long amount = 0;

        if (!jsonObject.has(serverId)) {
            return amount;
        }

        JsonObject server = jsonObject.get(serverId).getAsJsonObject();

        if (server.has("currentlyPlaying")) {
            amount++;
        }

        if (server.has("upComingSongs")) {
            amount += server.get("upComingSongs").getAsJsonObject().size();
        }

        return amount;
    }

    @Override
    public boolean hasNext(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        if (jsonObject.has(serverId) && jsonObject.get(serverId).getAsJsonObject().has("upComingSongs")) {
            return jsonObject.get(serverId).getAsJsonObject().get("upComingSongs").getAsJsonObject().size() >= 1;
        }
        return false;
    }

    @Override
    public boolean fixQueue(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        if (!jsonObject.has(serverId) || !jsonObject.get(serverId).getAsJsonObject().has("upComingSongs")) {
            return false;
        }

        JsonObject queue = jsonObject.get(serverId).getAsJsonObject().getAsJsonObject("upComingSongs");

        MultiValuedMap<String, Song> map = new ArrayListValuedHashMap<>();

        for (String queueId : queue.keySet()) {
            map.put(queueId, getSongFromJsonObject(queue.get(queueId).getAsJsonObject()));
            queue.remove(queueId);
        }

        for (Map.Entry<String, Song> entry : map.entries()) {
            JsonObject song = new JsonObject();

            song.addProperty("name", entry.getValue().name());
            song.addProperty("url", entry.getValue().url());
            song.addProperty("duration", entry.getValue().name());

            queue.add(entry.getKey(), song);
        }

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
        return true;
    }

    @Nullable
    @Override
    public Song getCurrentPlayingSong(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        if (jsonObject.has(serverId) && jsonObject.get(serverId).getAsJsonObject().has("currentlyPlaying")) {
            return getSongFromJsonObject(jsonObject.get(serverId).getAsJsonObject().get("currentlyPlaying").getAsJsonObject());
        }

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
        return null;
    }

    @Override
    public boolean deleteSongFromQueue(String serverId, long songId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        //Checks to see if the song is there
        if (!jsonObject.has(serverId) || !jsonObject.get(serverId).getAsJsonObject().has("upComingSongs") || !jsonObject.get(serverId).getAsJsonObject().get("upComingSongs").getAsJsonObject().has(String.valueOf(songId))) {
            return false;
        }

        jsonObject.get(serverId).getAsJsonObject().get("upComingSongs").getAsJsonObject().remove(String.valueOf(songId));

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
        return true;
    }

    @Override
    public boolean removeCurrentSong(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        if (!jsonObject.has(serverId) || jsonObject.get(serverId).getAsJsonObject().has("currentlyPlaying")) {
            return false;
        }

        jsonObject.get(serverId).getAsJsonObject().remove("currentlyPlaying");

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
        return true;
    }

    @Override
    public boolean pushNextSong(String serverId) {
        if (!hasNext(serverId)) {
            return false;
        }

        if (getQueue(serverId, 1, 1) == null && getQueue(serverId, 1, 1) == null) {
            return false;
        }

        Song song = null;

        for (Map.Entry<String, Song> entry : getQueue(serverId, 1, 1).entries()) {
            song = entry.getValue();
        }

        if (song == null) {
            return false;
        }

        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        if (jsonObject.get(serverId).getAsJsonObject().has("currentlyPlaying")) {
            jsonObject.get(serverId).getAsJsonObject().remove("currentlyPlaying");
        }

        JsonObject currentlyPlaying = new JsonObject();

        currentlyPlaying.addProperty("name", song.name());
        currentlyPlaying.addProperty("url", song.url());
        currentlyPlaying.addProperty("duration", song.duration());

        jsonObject.get(serverId).getAsJsonObject().add("currentlyPlaying", currentlyPlaying);

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
        return true;
    }

    @Override
    public boolean setNextSong(String serverId, Song song) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        JsonObject nextSong = new JsonObject();

        nextSong.addProperty("name", song.name());
        nextSong.addProperty("url", song.url());
        nextSong.addProperty("duration", song.duration());


        if (!jsonObject.has(serverId)) {

            JsonObject currentlyPlaying = new JsonObject();

            currentlyPlaying.add("currentlyPlaying", nextSong);

            jsonObject.add(serverId, currentlyPlaying);

            fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
            return true;
        }

        if (!jsonObject.get(serverId).getAsJsonObject().has("upComingSongs")) {
            jsonObject.get(serverId).getAsJsonObject().add("upComingSongs", nextSong);

            fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
            return true;
        }


        JsonObject oldQueue = jsonObject.get(serverId).getAsJsonObject().get("upComingSongs").getAsJsonObject().deepCopy();

        jsonObject.get(serverId).getAsJsonObject().remove("upComingSongs");

        JsonObject upComingSongs = new JsonObject();

        upComingSongs.add("0", nextSong);

        for (String id : oldQueue.keySet()) {
            if (Utils.parseLong(id) != -1) {
                upComingSongs.add(String.valueOf(Utils.parseLong(id) + 1), oldQueue.get(id));
            } else {
                jsonObject.get(serverId).getAsJsonObject().add("upComingSongs", oldQueue);
                return false;
            }
        }

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
        return true;
    }

    @Override
    public boolean addToQueue(String serverId, Song song) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSongsQueue()), JsonObject.class);

        JsonObject nextSong = new JsonObject();

        nextSong.addProperty("name", song.name());
        nextSong.addProperty("url", song.url());
        nextSong.addProperty("duration", song.duration());

        if (!jsonObject.has(serverId)) {

            JsonObject currentlyPlaying = new JsonObject();

            currentlyPlaying.add("currentlyPlaying", nextSong);

            jsonObject.add(serverId, currentlyPlaying);

            fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));

            return true;
        }

        if (!jsonObject.get(serverId).getAsJsonObject().has("upComingSongs")) {
            JsonObject upComingSongs = new JsonObject();

            upComingSongs.add("0", nextSong);

            jsonObject.get(serverId).getAsJsonObject().add("upComingSongs", upComingSongs);

            fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));
            return true;
        }

        JsonObject upComingSongs = jsonObject.get(serverId).getAsJsonObject().get("upComingSongs").getAsJsonObject();

        upComingSongs.add(String.valueOf(upComingSongs.size()), nextSong);

        fileManager.saveGsonSongsQueue(gson.fromJson(jsonObject, Map.class));

        return true;
    }

    private Song getSongFromJsonObject(JsonObject jsonObject) {
        return new Song(jsonObject.get("name").getAsString(), jsonObject.get("url").getAsString(), jsonObject.get("duration").getAsString());
    }
}
