package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;

public class Yeet extends ListenerAdapter implements Details {

    private final SystemData systemData;

    public Yeet(SystemData systemData) {
        this.systemData = systemData;
    }


    /**
     * it kicks every members outside of the voice chat channel.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        if (!systemData.hasMusicChannel(e.getGuild().getId()) || !systemData.getMusicChannel(e.getGuild().getId()).equals(e.getChannel().getId()))
            return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "yeet")) {
            if (e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) {
                e.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            } else {
                AudioChannel channel = e.getMember().getVoiceState().getChannel();

                for (Member member : channel.getMembers()) {
                    channel.getGuild().moveVoiceMember(member, null).queue();
                }
            }
            eraseCommand(systemData, e.getMessage(), e.getGuild().getId());

        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "yeet to kick all the members and the bot out of the current voice channel.";
    }
}
