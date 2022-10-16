package tech.seife.emily.datamanager.data.audio;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.collections4.MultiValuedMap;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class QueueManager {

    private final AudioData audioData;
    private final Map<String, Map<String, EmbedBuilder>> queueManager;
    private final Map<String, Integer> userPage;

    public QueueManager(AudioData audioData) {
        this.audioData = audioData;
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

    public void clearSongs(String serverId) {
        audioData.clearSongs(serverId);
    }

    public MultiValuedMap<String, Song> getQueuedSongs(String serverId, long startingPlace, long howManyToGet) {
        return audioData.getQueue(serverId, startingPlace, howManyToGet);

    }

    public EmbedBuilder getEmbedQueue(long startingNumber, int currentPage, JDA jda, EmbedBuilder embedBuilder, String serverId) {
        if (startingNumber < 0) return null;

        StringBuilder sb = new StringBuilder();


        embedBuilder.setTitle("Queue for " + jda.getSelfUser().getName());

        sb.append("Currently playing: ").append(String.format("[%s](%s)", getCurrentSongName(serverId), getCurrentSongUrl(serverId))).append("\n\n\n\n");

        MultiValuedMap<String, Song> songs = getQueuedSongs(serverId, startingNumber - 1, 9);

        for (Map.Entry<String, Song> entry : songs.entries()) {
            sb.append(startingNumber + 1).append(". ").append(String.format("[%s](%s)", entry.getKey(), entry.getValue())).append("\n");
            startingNumber++;
        }

        embedBuilder.setFooter("Total songs: " + getAmountOfRemainingSongs(serverId) + " page: " + (currentPage + 1) + " / " + (int) (Math.ceil(getAmountOfRemainingSongs(serverId) / 10) + 1));
        embedBuilder.setDescription(sb);

        return embedBuilder;
    }


    public long getAmountOfRemainingSongs(String serverId) {
        return audioData.getAmountOfRemainingSongs(serverId);
    }

    public boolean hasNext(String serverId) {
        return audioData.hasNext(serverId);
    }

    public void fixQueue(String serverId) {
        audioData.fixQueue(serverId);
    }

    public String getCurrentSongName(String serverId) {
        return audioData.getCurrentPlayingSong(serverId).name();
    }


    public String getCurrentSongUrl(String serverId) {
        return audioData.getCurrentPlayingSong(serverId).url();
    }

    public void deleteQueueSong(String serverId, int songId) {
        audioData.deleteSongFromQueue(serverId, songId);
    }

    public void removeCurrentSong(String serverId) {
        audioData.removeCurrentSong(serverId);
    }

    public void pushNextSong(String serverId) {
        audioData.pushNextSong(serverId);

    }

    public void setNextSong(String serverId, Song song) {
        audioData.setNextSong(serverId, song);

    }

    public void addToQueue(String serverId, Song song) {
        audioData.addToQueue(serverId, song);

    }
}
