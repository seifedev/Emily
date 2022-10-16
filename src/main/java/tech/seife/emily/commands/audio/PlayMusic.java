package tech.seife.emily.commands.audio;

import com.google.api.services.youtube.model.VideoListResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.seife.emily.audiologic.*;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.audio.AudioData;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.audio.Song;
import tech.seife.emily.datamanager.data.system.SystemData;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class PlayMusic extends ListenerAdapter implements Details {

    private final SystemData systemData;
    private final AudioPlayerManager audioPlayerManager;
    private final QueueManager queueManager;
    private final AudioData audioData;
    private final GuildAudioPlayer guildAudioPlayer;

    public PlayMusic(SystemData systemData, AudioPlayerManager audioPlayerManager, QueueManager queueManager, AudioData audioData, GuildAudioPlayer guildAudioPlayer) {
        this.systemData = systemData;
        this.audioPlayerManager = audioPlayerManager;
        this.queueManager = queueManager;
        this.audioData = audioData;
        this.guildAudioPlayer = guildAudioPlayer;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        if (!systemData.hasMusicChannel(e.getGuild().getId()) || !systemData.getMusicChannel(e.getGuild().getId()).equals(e.getChannel().getId()))
            return;

        if (e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) return;

        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length >= 2 && message[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "play")) {

            VideoListResponse video = getVideoListResponse(message, e.getGuild().getId());

            if (!guildAudioPlayer.dataExists(e.getGuild().getId())) {

                AudioEventAdapter audioEventAdapter = new TruckScheduler(audioPlayerManager, audioData, guildAudioPlayer);

                guildAudioPlayer.addGuild(e.getGuild().getId(), audioEventAdapter);

                AudioManager audioManager = e.getGuild().getAudioManager();

                audioManager.setSendingHandler(new AudioPlayerSendHandler(guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer()));

                audioManager.setSelfDeafened(true);
                audioManager.setSelfMuted(false);


                audioPlayerManager.loadItem(getUrl(video.getItems().get(0).getId()), new AudioResults(audioPlayerManager, guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer()));
                queueManager.setNextSong(e.getGuild().getId(), getSongFromVideoListResponse(video));
            } else {
                queueManager.addToQueue(e.getGuild().getId(), getSongFromVideoListResponse(video));
            }

            eraseCommand(systemData, e.getMessage(), e.getGuild().getId());

            e.getChannel().sendMessageEmbeds(getSuccessMessage(video.getItems().get(0).getSnippet().getTitle(), video.getItems().get(0).getContentDetails().getDuration(), getUrl(video.getItems().get(0).getId())).build()).queue();
        }


    }

    /**
     * @param songName The name of the song
     * @param duration The duration of the song in a readable form
     * @param url      The URL of the song
     * @return an EmbedBuilder that contains the success message.
     */
    private EmbedBuilder getSuccessMessage(String songName, String duration, String url) {
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
    private VideoListResponse getVideoListResponse(String[] message, String serverId) {
        RetrieveVideo retrieveVideo = new RetrieveVideo(systemData);


        VideoListResponse videoListResponse = null;
        try {
            videoListResponse = retrieveVideo.getVideo(getSong(message), serverId);
        } catch (IOException e) {
            // TODO: 15/10/2022 Better error handling.
            System.out.println(e.getMessage());
        }

        return videoListResponse;
    }

    private String getUrl(String id) {
        String yt = "https://www.youtube.com/watch?v=" + id;

        return yt;
    }

    /**
     * @param message The message that the member sent.
     * @return the URL or the song name from the member's message.
     */
    private String getSong(String[] message) {
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
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "play <Name of the Song>, <video ID> or <URL> to play a song in the audio channel.";
    }

    private Song getSongFromVideoListResponse(VideoListResponse video) {
        return new Song(video.getItems().get(0).getSnippet().getTitle(), video.getItems().get(0).getId(), video.getItems().get(0).getContentDetails().getDuration());
    }

}
