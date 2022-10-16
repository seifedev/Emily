package tech.seife.emily.datamanager.data.audio;

import org.apache.commons.collections4.MultiValuedMap;
import tech.seife.emily.commands.audio.SkipSong;

import javax.annotation.Nullable;

public interface AudioData {

    boolean clearSongs(String serverId);

    @Nullable
    MultiValuedMap<String, Song> getQueue(String serverId, long startingPlace, long howManyToGet);

    long getAmountOfRemainingSongs(String serverId);

    boolean hasNext(String serverId);

    boolean fixQueue(String serverId);

    Song getCurrentPlayingSong(String serverId);

    boolean deleteSongFromQueue(String serverId, long songId);

    boolean removeCurrentSong(String serverId);

    boolean pushNextSong(String serverId);

    boolean setNextSong(String serverId, Song song);

    boolean addToQueue(String serverId, Song song);
}
