package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class Yeet extends ListenerAdapter implements Details {

    private final SystemManager systemData;

    public Yeet(SystemManager systemData) {
        this.systemData = systemData;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!systemData.getMusicChannel().equals(systemData.getMusicChannel())) return;
        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix() + "yeet")) {
            if (e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) {
                e.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            } else {
                AudioChannel channel = e.getMember().getVoiceState().getChannel();

                for (Member member : channel.getMembers()) {
                    channel.getGuild().moveVoiceMember(member, null).queue();
                }
            }
            eraseCommand(systemData, e.getMessage());
        }
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "yeet to kick all the members and the bot out of the current voice channel.";
    }
}
