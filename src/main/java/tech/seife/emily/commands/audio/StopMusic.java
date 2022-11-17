package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.system.SystemData;

public class StopMusic extends ListenerAdapter{

    private final SystemData systemData;
    private final QueueManager queueManager;
    private final GuildAudioPlayer guildAudioPlayer;
    private final Messenger messenger;

    public StopMusic(SystemData systemData, QueueManager queueManager, GuildAudioPlayer guildAudioPlayer, Messenger messenger) {
        this.systemData = systemData;
        this.queueManager = queueManager;
        this.guildAudioPlayer = guildAudioPlayer;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("stop") || !canRunCommand(e)) return;

        guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().getPlayingTrack().stop();
        queueManager.clearSongs(e.getGuild().getId());
    }

    private boolean canRunCommand(@NotNull SlashCommandInteractionEvent e) {
        if (!systemData.hasMusicChannel(e.getGuild().getId())) {
            e.replyEmbeds(messenger.getMessageEmbed("notInVoiceChat")).queue();
            return false;
        } else if (!systemData.getMusicChannel(e.getGuild().getId()).equals(e.getChannel().getId())) {
            e.replyEmbeds(messenger.getMessageEmbed("notAMusicChannel")).queue();
            return false;
        } else if (e.getMember() == null || e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) {
            e.replyEmbeds(messenger.getMessageEmbed("notInVoiceChat")).queue();
            return false;
        } else if (e.getSubcommandGroup() == null || e.getSubcommandGroup().split(" ").length != 1) {
            e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        } else if (guildAudioPlayer.dataExists(e.getGuild().getId())) {
            e.replyEmbeds(messenger.getMessageEmbed("notPlayingMusic")).queue();
        }
        return true;
    }
}
