package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;
import tech.seife.emily.utils.Utils;

public class SelfDestructDelay extends ListenerAdapter implements Details {

    private final SystemData systemData;

    public SelfDestructDelay(SystemData systemData) {
        this.systemData = systemData;
    }

    /**
     * if the self-destruct is enabled it will delete the commands of the members
     * every X amount of time to keep the chat clean
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length != 2 && Utils.hasAdmin(e.getMember())) return;

        if (message[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "selfDestructDelay")) {
            if (getNumber(message[1]) != -1) {
                systemData.setSelfDestructDelay(e.getGuild().getId(), getNumber(message[1]));
                                eraseCommand(systemData, e.getMessage(), e.getGuild().getId());

            }
        }
    }

    private int getNumber(String argument) {
        try {
            return Integer.valueOf(argument);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "selfDestructDelay <seconds>, to delete the command after X seconds";
    }
}
