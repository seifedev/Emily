package tech.seife.emily.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class StopMusic extends ListenerAdapter implements Details {

    private final SystemManager systemData;
    private final AudioPlayer audioPlayer;
    private final QueueManager queueManager;

    public StopMusic(SystemManager systemData, AudioPlayer audioPlayer, QueueManager queueManager) {
        this.systemData = systemData;
        this.audioPlayer = audioPlayer;
        this.queueManager = queueManager;
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!systemData.getMusicChannel().equals(systemData.getMusicChannel())) return;
        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix() + "stop")) {
            if (audioPlayer.getPlayingTrack() != null) {
                audioPlayer.getPlayingTrack().stop();
                queueManager.clearSongs();
                eraseCommand(systemData, e.getMessage());
                e.getChannel().sendMessageEmbeds(getClearedEmbed().build()).queue();
            }
        }
    }

    private EmbedBuilder getClearedEmbed() {
        EmbedBuilder builder = new EmbedBuilder();


        builder.setTitle("Stopping...");
        builder.setDescription("Music has been stopped & queue has been cleared.");

        return builder;
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "pause, to stop the music and clean the queue.";
    }

}
