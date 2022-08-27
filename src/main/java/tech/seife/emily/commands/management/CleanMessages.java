package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CleanMessages extends ListenerAdapter implements Details {
    private final SystemManager systemData;

    public CleanMessages(SystemManager systemData) {
        this.systemData = systemData;
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length == 2 && Utils.hasManageChannel(e.getMember())) {
            List<Message> messagesToDelete = new ArrayList();
            if (this.canClean(message[0], message[1])) {
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
                    this.eraseCommand(this.systemData, e.getMessage());
                }
            }

        }
    }

    private boolean canClean(String command, String numberToParse) {
        if (command.equals(this.systemData.getCommandPrefix() + "clean")) {
            return Utils.parseLong(numberToParse) != 1L;
        } else {
            return false;
        }
    }

    public String getExplanation() {
        return this.systemData.getCommandPrefix() + "clean <amount> to delete an <amount> of messages in this channel";
    }
}
