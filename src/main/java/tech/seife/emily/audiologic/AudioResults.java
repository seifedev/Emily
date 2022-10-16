package tech.seife.emily.audiologic;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioResults implements AudioLoadResultHandler {

    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;

    public AudioResults(AudioPlayerManager audioPlayerManager, AudioPlayer audioPlayer) {
        this.audioPlayerManager = audioPlayerManager;
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        audioPlayer.playTrack(audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {

    }

    @Override
    public void noMatches() {
        System.out.println("Couldn't find match");
    }

    @Override
    public void loadFailed(FriendlyException e) {
        System.out.println("Failed to load music.");

    }
}