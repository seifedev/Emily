package tech.seife.emily.commands.entertainment;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;

public class BotTalk extends ListenerAdapter implements Details {

    private final SystemData systemData;

    public BotTalk(SystemData systemData) {
        this.systemData = systemData;
    }

    /**
     *
     * Make the bot talk and send a message that the member defined
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "say")) {
            e.getChannel().sendMessage(getMessage(message)).queue();
                            eraseCommand(systemData, e.getMessage(), e.getGuild().getId());

        }
    }

    /**
     *
     * @param arguments What the bot should say
     * @return Returns the message that the bot should send
     */
    private String getMessage(String[] arguments) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < arguments.length; i++) {
            sb.append(arguments[i]).append(" ");
        }
        return sb.toString();
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "say, the bot will send a message with the arguments you provided";
    }
}
