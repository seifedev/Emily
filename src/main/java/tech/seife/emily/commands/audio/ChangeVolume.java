package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.datamanager.data.system.SystemData;

public class ChangeVolume extends ListenerAdapter {

    private final SystemData systemData;
    private final GuildAudioPlayer guildAudioPlayer;
    private final Messenger messenger;

    public ChangeVolume(SystemData systemData, GuildAudioPlayer guildAudioPlayer, Messenger messenger) {
        this.systemData = systemData;
        this.guildAudioPlayer = guildAudioPlayer;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("changeVolume")) return;
        if (!canRunCommand(e)) return;
        if (guildAudioPlayer.dataExists(e.getGuild().getId())) {
            guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().setVolume(e.getOption("percentage").getAsInt());
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
        } else if (e.getOptions().size() != 1 || e.getOption("percentage") == null) {
            e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        }
        return true;
    }
}
