package tech.seife.emily.audiologic;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import tech.seife.emily.datamanager.data.audio.AudioData;

public class TruckScheduler extends AudioEventAdapter {

    private final AudioPlayerManager audioPlayerManager;
    private final AudioData audioData;
    private final GuildAudioPlayer guildAudioPlayer;


    public TruckScheduler(AudioPlayerManager audioPlayerManager, AudioData audioData, GuildAudioPlayer guildAudioPlayer) {
        this.audioPlayerManager = audioPlayerManager;
        this.audioData = audioData;
        this.guildAudioPlayer = guildAudioPlayer;
    }


    @Override
    public void onPlayerPause(AudioPlayer player) {
        super.onPlayerPause(player);
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        super.onPlayerResume(player);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        super.onTrackStart(player, track);
    }


    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        super.onTrackEnd(player, track, endReason);
        if (endReason.mayStartNext && guildAudioPlayer.getAudioData(player) != null) {
            String serverId = guildAudioPlayer.getAudioData(player).serverId();
            audioData.removeCurrentSong(serverId);
            if (audioData.hasNext(serverId)) {
                audioData.pushNextSong(serverId);
                audioData.deleteSongFromQueue(serverId, 0);
                audioData.fixQueue(serverId);
                audioPlayerManager.loadItem(audioData.getCurrentPlayingSong(serverId).url(), new AudioResults(audioPlayerManager, player));

            }
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        super.onTrackException(player, track, exception);
    }
}
