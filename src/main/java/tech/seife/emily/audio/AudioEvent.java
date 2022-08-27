package tech.seife.emily.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import tech.seife.emily.datamanager.data.audio.QueueManager;

public class AudioEvent extends AudioEventAdapter {

    private final AudioPlayerManager audioPlayerManager;
    private final QueueManager queueManager;


    public AudioEvent(AudioPlayerManager audioPlayerManager, QueueManager queueManager) {
        this.audioPlayerManager = audioPlayerManager;
        this.queueManager = queueManager;
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
        if (endReason.mayStartNext) {
            queueManager.removeCurrentSong();
            if (queueManager.hasNext()) {
                queueManager.pushNextSong();
                queueManager.deleteQueueSong(0);
                queueManager.rePosition();
                audioPlayerManager.loadItem(queueManager.getCurrentSongUrl(), new AudioResult(audioPlayerManager, player));

            }

        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        super.onTrackException(player, track, exception);
    }
}
