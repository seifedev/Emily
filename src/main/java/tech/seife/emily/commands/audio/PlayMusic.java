package tech.seife.emily.commands.audio;

import com.google.api.services.youtube.model.VideoListResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.seife.emily.audio.AudioResult;
import tech.seife.emily.audio.RetrieveVideo;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.system.SystemManager;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class PlayMusic extends ListenerAdapter implements Details {

    private final SystemManager systemData;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private final QueueManager queueManager;

    public PlayMusic(SystemManager systemData, AudioPlayerManager audioPlayerManager, AudioPlayer audioPlayer, QueueManager queueManager) {
        this.systemData = systemData;
        this.audioPlayerManager = audioPlayerManager;
        this.audioPlayer = audioPlayer;
        this.queueManager = queueManager;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!systemData.getMusicChannel().equals(systemData.getMusicChannel())) return;

        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length >= 2 && message[0].equalsIgnoreCase(systemData.getCommandPrefix() + "play")) {
            if (e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) return;

            VideoListResponse video = getVideoListResponse(message);

            if (audioPlayer.getPlayingTrack() == null) {
                audioPlayerManager.loadItem(getUrl(video.getItems().get(0).getId()), new AudioResult(audioPlayerManager, audioPlayer));
                queueManager.addCurrentSong(video.getItems().get(0).getSnippet().getTitle(), video.getItems().get(0).getId(), video.getItems().get(0).getContentDetails().getDuration());
            } else {
                queueManager.addToQueue(video.getItems().get(0).getSnippet().getTitle(), video.getItems().get(0).getId(), video.getItems().get(0).getContentDetails().getDuration());
            }
            eraseCommand(systemData, e.getMessage());
            e.getChannel().sendMessageEmbeds(getPlayMessage(video.getItems().get(0).getSnippet().getTitle(), video.getItems().get(0).getContentDetails().getDuration(), getUrl(video.getItems().get(0).getId())).build()).queue();

        }


    }

    private EmbedBuilder getPlayMessage(String songName, String duration, String url) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.CYAN);
        builder.setTitle("Song Queried: ");
        builder.setDescription(String.format("[%s](%s)", songName, url));
        builder.setFooter("Track length: " + getReadableDuration(Duration.parse(duration).toSeconds()));

        return builder;
    }

    private String getReadableDuration(long seconds) {

        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24L);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long secs = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);


        return hours + "H " + minutes + "M " + secs + "s ";

    }

    @Nullable
    private VideoListResponse getVideoListResponse(String[] message) {
        RetrieveVideo retrieveVideo = new RetrieveVideo();

        VideoListResponse videoListResponse = null;
        try {
            videoListResponse = retrieveVideo.getVideo(getInput(message));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return videoListResponse;
    }

    private String getUrl(String id) {
        String yt = "https://www.youtube.com/watch?v=" + id;

        return yt;
    }

    private String getInput(String[] message) {
        if (message.length >= 3) {
            StringBuilder sb = new StringBuilder();

            for (int i = 1; i < message.length; i++) {
                sb.append(message[i]).append(" ");
            }
            return sb.toString();
        }
        return message[1];
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "play <Name of the Song>, <video ID> or <URL> to play a song in the audio channel.";
    }

}
