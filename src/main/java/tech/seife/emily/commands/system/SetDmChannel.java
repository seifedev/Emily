package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

public class SetDmChannel extends ListenerAdapter implements Details {

    private final SystemManager systemData;

    public SetDmChannel(SystemManager systemData) {
        this.systemData = systemData;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] messages = event.getMessage().getContentRaw().split(" ");

        if (messages.length != 1 && !Utils.hasAdmin(event.getMember())) return;

        if (messages[0].equalsIgnoreCase(systemData.getCommandPrefix() + "setDmChannel")) {
            systemData.setDmChannel(event.getChannel().getId());
            eraseCommand(systemData, event.getMessage());
        }
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "setDmChannel, to set a channel receive all the DMs that the bot receives";
    }
}
