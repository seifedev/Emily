package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.system.SystemData;

public class Yeet extends ListenerAdapter {

    private final SystemData systemData;
    private final Messenger messenger;

    public Yeet(SystemData systemData, Messenger messenger) {
        this.systemData = systemData;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("yeet")) return;
        if (!canRunCommand(e)) return;

        AudioChannel audioChannel = e.getChannel().asVoiceChannel();

        for (Member member : audioChannel.getMembers()) {
            audioChannel.getGuild().moveVoiceMember(member, null);
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
        } else if (e.getSubcommandGroup() != null) {
             e.replyEmbeds(messenger.getMessageEmbed("doesntTakeArguments")).queue();
            return false;
        }
        return true;
    }

}
