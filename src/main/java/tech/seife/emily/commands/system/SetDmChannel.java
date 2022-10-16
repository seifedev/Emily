package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;
import tech.seife.emily.utils.Utils;

public class SetDmChannel extends ListenerAdapter implements Details {

    private final SystemData systemData;

    public SetDmChannel(SystemData systemData) {
        this.systemData = systemData;
    }

    /**
     * Sets a channel that prints the private messages between a member and the BOT
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] messages = e.getMessage().getContentRaw().split(" ");

        if (messages.length != 1 && !Utils.hasAdmin(e.getMember())) return;

        if (messages[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "setDmChannel")) {
            systemData.setDmChannel(e.getGuild().getId(), e.getChannel().getId());
            eraseCommand(systemData, e.getMessage(), e.getGuild().getId());
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "setDmChannel, to set a channel receive all the DMs that the bot receives";
    }
}
