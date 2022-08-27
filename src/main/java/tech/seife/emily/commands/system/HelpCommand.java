package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Commands;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class HelpCommand extends ListenerAdapter implements Details {

    private final SystemManager systemData;
    private final Commands commands;

    public HelpCommand(SystemManager systemData, Commands commands) {
        this.systemData = systemData;
        this.commands = commands;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix() + "help")) {
            StringBuilder sb = new StringBuilder();

            for (Details command : commands.getCommands()) {
                sb.append(command.getExplanation()).append("\n");
            }

            e.getMember().getUser().openPrivateChannel()
                    .flatMap(channel -> channel.sendMessage(sb.toString())).queue();
            eraseCommand(systemData, e.getMessage());

        }
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "help, sends a private message with all the commands and their explanations.";
    }
}
