package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

public class SkipPartsOfMusic extends ListenerAdapter {

    private final SystemData systemData;
    private final GuildAudioPlayer guildAudioPlayer;
    private final Messenger messenger;

    public SkipPartsOfMusic(SystemData systemData, GuildAudioPlayer guildAudioPlayer, Messenger messenger) {
        this.systemData = systemData;
        this.guildAudioPlayer = guildAudioPlayer;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("seek")) return;
        if (!canRunCommand(e)) return;

        long parsedNumber = e.getOption("seconds").getAsLong();

        if (parsedNumber == -1) {
            e.replyEmbeds(messenger.getMessageEmbed("w*")).queue();
        }

        guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().getPlayingTrack().setPosition((guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().getPlayingTrack().getPosition() + parsedNumber * 60L));
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
        } else if (e.getOptions().isEmpty() || e.getOption("seek") == null) {
            e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        } else if (guildAudioPlayer.dataExists(e.getGuild().getId())) {
            e.replyEmbeds(messenger.getMessageEmbed("notPlayingMusic")).queue();
        }
        return true;
    }
}
