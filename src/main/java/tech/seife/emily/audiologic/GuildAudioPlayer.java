package tech.seife.emily.audiologic;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class GuildAudioPlayer {

    private final Set<AudioGuildData> audioGuildDataSet;
    private final AudioPlayerManager audioManager;


    public GuildAudioPlayer(AudioPlayerManager audioManager) {
        this.audioManager = audioManager;
        audioGuildDataSet = new HashSet<>();
    }

    public void addGuild(String serverId, AudioEventAdapter audioEventAdapter) {
        if (!dataExists(serverId)) {

            AudioPlayer audioPlayer = audioManager.createPlayer();
            AudioSourceManagers.registerRemoteSources(audioManager);

            audioPlayer.addListener(audioEventAdapter);

            audioGuildDataSet.add(new AudioGuildData(serverId, audioPlayer, audioEventAdapter));
        }
    }

    @Nullable
    public AudioGuildData getAudioData(String serverId) {
        return audioGuildDataSet.stream().filter(audioGuildData -> audioGuildData.serverId().equals(serverId)).findFirst().orElse(null);
    }

    @Nullable
    public AudioGuildData getAudioData(AudioPlayer audioPlayer) {
        return audioGuildDataSet.stream().filter(audioGuildData -> audioGuildData.audioPlayer().equals(audioPlayer)).findFirst().orElse(null);
    }

    public boolean dataExists(String serverId) {
        return audioGuildDataSet.stream().anyMatch(audioGuildData -> audioGuildData.serverId().equals(serverId));
    }
}
