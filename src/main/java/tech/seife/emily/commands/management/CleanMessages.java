package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CleanMessages extends ListenerAdapter {
    private final SystemData systemData;
    private final Messenger messenger;

    public CleanMessages(SystemData systemData, Messenger messenger) {
        this.systemData = systemData;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("clean") || !canRunCommand(e)) return;
        List<Message> messagesToDelete = new ArrayList();

        long howManyToDelete = e.getOption("amount").getAsLong();

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
        }

    }

    private boolean canRunCommand(SlashCommandInteractionEvent e) {
        if (e.getOptions().size() != 1) {
            e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        } else if (!Utils.hasManageChannel(e.getMember())) {
            e.replyEmbeds(messenger.getMessageEmbed("noPermission")).queue();
            return false;
        }
        return true;
    }
}
