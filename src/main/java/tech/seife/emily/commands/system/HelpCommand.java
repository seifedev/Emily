package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Commands;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;

public class HelpCommand extends ListenerAdapter implements Details {

    private final SystemData systemData;
    private final Commands commands;

    public HelpCommand(SystemData systemData, Commands commands) {
        this.systemData = systemData;
        this.commands = commands;
    }

    /**
     * Sends a private message to the one who iniated the command with all commands of the bot.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "help")) {
            StringBuilder sb = new StringBuilder();

            for (Details command : commands.getCommands()) {
                sb.append(command.getExplanation(e.getGuild().getId())).append("\n");
            }

            e.getMember().getUser().openPrivateChannel()
                    .flatMap(channel -> channel.sendMessage(sb.toString())).queue();
            eraseCommand(systemData, e.getMessage(), e.getGuild().getId());

        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "help, sends a private message with all the commands and their explanations.";
    }
}
