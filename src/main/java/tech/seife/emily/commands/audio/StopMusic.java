package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.system.SystemData;

public class StopMusic extends ListenerAdapter implements Details {

    private final SystemData systemData;
    private final QueueManager queueManager;
    private final GuildAudioPlayer guildAudioPlayer;

    public StopMusic(SystemData systemData, QueueManager queueManager, GuildAudioPlayer guildAudioPlayer) {
        this.systemData = systemData;
        this.queueManager = queueManager;
        this.guildAudioPlayer = guildAudioPlayer;
    }


    /**
     * Stops the bot from broadcasting any songs.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        if (!systemData.hasMusicChannel(e.getGuild().getId()) || !systemData.getMusicChannel(e.getGuild().getId()).equals(e.getChannel().getId()))
            return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "stop")) {
            if (guildAudioPlayer.getAudioData(e.getGuild().getId()) != null && guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().getPlayingTrack() != null) {
                guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().getPlayingTrack().stop();
                queueManager.clearSongs(e.getGuild().getId());
                eraseCommand(systemData, e.getMessage(), e.getGuild().getId());

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
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "pause, to stop the music and clean the queue.";
    }

}
