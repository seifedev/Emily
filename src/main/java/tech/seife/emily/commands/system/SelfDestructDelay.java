package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

public class SelfDestructDelay extends ListenerAdapter implements Details {

    private final SystemManager systemData;

    public SelfDestructDelay(SystemManager systemData) {
        this.systemData = systemData;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length != 2 && Utils.hasAdmin(e.getMember())) return;

        if (message[0].equalsIgnoreCase(systemData.getCommandPrefix() + "selfDestructDelay")) {
            if (getNumber(message[1]) != -1) {
                systemData.setSelfDestructDelay(getNumber(message[1]));
                eraseCommand(systemData, e.getMessage());
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
    public String getExplanation() {
        return systemData.getCommandPrefix() + "selfDestructDelay <seconds>, to delete the command after X seconds";
    }
}
