package tech.seife.emily.datamanager.data.audio;

import org.apache.commons.collections4.MultiValuedMap;
import tech.seife.emily.commands.audio.SkipSong;

import javax.annotation.Nullable;

public interface AudioData {

    /**
     * @param serverId The ID of the server.
     * @return
     */
    boolean clearSongs(String serverId);


    /**
     * @param serverId The ID of the server.
     * @param startingPlace From which song to start fetching the remaining ones.
     * @param howManyToGet How many songs to grab in total.
     * @return May return null or a lit of songs.
     */
    @Nullable
    MultiValuedMap<String, Song> getQueue(String serverId, long startingPlace, long howManyToGet);

    /**
     * @param serverId The ID of server.
     * @return The amount of songs that are left in the queue.
     */
    long getAmountOfRemainingSongs(String serverId);

    /**
     * @param serverId The ID of server.
     * @return true if there are more songs left in the queue, false otherwise.
     */
    boolean hasNext(String serverId);


    /**
     * @param serverId The ID of server.
     * @return if the queue has been fixed.
     */
    boolean fixQueue(String serverId);

    /**
     * @param serverId The ID of server.
     * @return The song that is currently being played.
     */
    @Nullable
    Song getCurrentPlayingSong(String serverId);

    /**
     * @param serverId The ID of server.
     * @param songId the ID of the song.
     * @return true if the deletion was successful, otherwise false.
     */
    boolean deleteSongFromQueue(String serverId, long songId);

    /**
     * @param serverId The ID of server.
     * @return If it was able to stop and remove the current playing song.
     */
    boolean removeCurrentSong(String serverId);

    /**
     * @param serverId The ID of server.
     * @return true if it was able to push the next song in the queue(if there was one), otherwise false.
     */
    boolean pushNextSong(String serverId);

    /**
     * @param serverId The ID of server.
     * @param song The song to play after the current playing song has been finished.
     * @return true If the next song has been set successfully, otherwise false.
     */
    boolean setNextSong(String serverId, Song song);

    /**
     * @param serverId The ID of server.
     * @param song The song to add at the queue.
     * @return true if it was added successfully.
     */
    boolean addToQueue(String serverId, Song song);
}
