package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;
import tech.seife.emily.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CleanMessages extends ListenerAdapter implements Details {
    private final SystemData systemData;

    public CleanMessages(SystemData systemData) {
        this.systemData = systemData;
    }

    /**
     * Attempts to delete a certain amount of messages from the channel,
     * The following method isn't perfect because of the limitations
     * of POST operations that the discord imposes us.
     */
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length == 2 && Utils.hasManageChannel(e.getMember())) {
            List<Message> messagesToDelete = new ArrayList();

            if (this.canClean(message[0], message[1], e.getGuild().getId())) {
                long howManyToDelete = Utils.parseLong(message[1]);

                if (e.getChannel().getIterableHistory().stream().toList() != null) {
                    List<Message> messages = e.getChannel().getIterableHistory().stream().toList();
                    if (howManyToDelete >= (long) messages.size()) {
                        howManyToDelete = messages.size();
                    }

                    for (int i = 0; (long) i < howManyToDelete; ++i) {
                        messagesToDelete.add(messages.get(i));
                    }

                    messagesToDelete.forEach((toDelete) -> {
                        e.getChannel().deleteMessageById(toDelete.getId()).queue();
                    });

                    this.eraseCommand(this.systemData, e.getMessage(), e.getGuild().getId());
                }
            }

        }
    }

    /**
     * Checks to see if the command is structured right.
     *
     * @param command       The whole input of the member
     * @param numberToParse The potential amount of channels that needed to be deleted.
     * @param serverId the ID of the server.
     * @return true if it can clear, false otherwise
     */
    private boolean canClean(String command, String numberToParse, String serverId) {
        if (command.equals(this.systemData.getCommandPrefix(serverId) + "clean")) {
            return Utils.parseLong(numberToParse) != 1L;
        } else {
            return false;
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return this.systemData.getCommandPrefix(serverId) + "clean <amount> to delete an <amount> of messages in this channel";
    }
}
