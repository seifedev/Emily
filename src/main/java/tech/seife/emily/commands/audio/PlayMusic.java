package tech.seife.emily.commands.audio;

import com.google.api.services.youtube.model.VideoListResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.seife.emily.Messenger;
import tech.seife.emily.audiologic.*;
import tech.seife.emily.datamanager.data.audio.AudioData;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.audio.Song;
import tech.seife.emily.datamanager.data.system.SystemData;

import java.io.IOException;

public class PlayMusic extends ListenerAdapter {

    private final SystemData systemData;
    private final AudioPlayerManager audioPlayerManager;
    private final QueueManager queueManager;
    private final AudioData audioData;
    private final GuildAudioPlayer guildAudioPlayer;
    private final Messenger messenger;

    public PlayMusic(SystemData systemData, AudioPlayerManager audioPlayerManager, QueueManager queueManager, AudioData audioData, GuildAudioPlayer guildAudioPlayer, Messenger messenger) {
        this.systemData = systemData;
        this.audioPlayerManager = audioPlayerManager;
        this.queueManager = queueManager;
        this.audioData = audioData;
        this.guildAudioPlayer = guildAudioPlayer;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("playmusic")) return;
        if (!canRunCommand(e)) return;


        String[] userResponse = e.getOption("song").getAsString().split(" ");

        VideoListResponse response = getVideoListResponse(userResponse, e.getGuild().getId());

        if (!guildAudioPlayer.dataExists(e.getGuild().getId())) {

            AudioEventAdapter audioEventAdapter = new TruckScheduler(audioPlayerManager, audioData, guildAudioPlayer);

            guildAudioPlayer.addGuild(e.getGuild().getId(), audioEventAdapter);

            AudioManager audioManager = e.getGuild().getAudioManager();

            audioManager.setSendingHandler(new AudioPlayerSendHandler(guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer()));

            audioManager.setSelfDeafened(true);
            audioManager.setSelfMuted(false);

            audioPlayerManager.loadItem(getUrl(response.getItems().get(0).getId()), new AudioResults(audioPlayerManager, guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer()));
            queueManager.setNextSong(e.getGuild().getId(), getSongFromVideoListResponse(response));
            e.replyEmbeds(messenger.sendPlayingSong(response.getItems().get(0).getSnippet().getTitle(), response.getItems().get(0).getContentDetails().getDuration(), getUrl(response.getItems().get(0).getId()))).queue();
        } else {
            queueManager.addToQueue(e.getGuild().getId(), getSongFromVideoListResponse(response));
            e.replyEmbeds(messenger.addedToQueue(response.getItems().get(0).getSnippet().getTitle(), response.getItems().get(0).getContentDetails().getDuration(), getUrl(response.getItems().get(0).getId()))).queue();
        }
    }

    private boolean canRunCommand(@NotNull SlashCommandInteractionEvent e) {
        if (!systemData.hasMusicChannel(e.getGuild().getId())) {
             e.replyEmbeds(messenger.getMessageEmbed("createMusicChannel")).queue();
            return false;
        } else if (!systemData.getMusicChannel(e.getGuild().getId()).equals(e.getChannel().getId())) {
             e.replyEmbeds(messenger.getMessageEmbed("notAMusicChannel")).queue();
            return false;
        } else if (e.getMember() == null || e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) {
             e.replyEmbeds(messenger.getMessageEmbed("notInVoiceChat")).queue();
            return false;
        } else if (e.getOption("song") == null || e.getOption("song").getAsString().split(" ").length == 0) {
             e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        }
        return true;
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

    private Song getSongFromVideoListResponse(VideoListResponse video) {
        return new Song(video.getItems().get(0).getSnippet().getTitle(), video.getItems().get(0).getId(), video.getItems().get(0).getContentDetails().getDuration());
    }

}
