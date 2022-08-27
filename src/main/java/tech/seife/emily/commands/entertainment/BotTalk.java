package tech.seife.emily.commands.entertainment;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class BotTalk extends ListenerAdapter implements Details {

    private final SystemManager systemData;

    public BotTalk(SystemManager systemData) {
        this.systemData = systemData;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase(systemData.getCommandPrefix() + "say")) {
            e.getChannel().sendMessage(getMessage(message)).queue();
            eraseCommand(systemData, e.getMessage());
        }
    }

    private String getMessage(String[] arguments) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < arguments.length; i++) {
            sb.append(arguments[i]).append(" ");
        }
        return sb.toString();
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "say, the bot will send a message with the arguments you provided";
    }
}
