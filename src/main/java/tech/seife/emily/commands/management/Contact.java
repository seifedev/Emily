package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

import java.util.List;

public class Contact extends ListenerAdapter {
    private final JDA jda;
    private final SystemData systemData;
    private final Messenger messenger;

    public Contact(JDA jda, SystemData systemData, Messenger messenger) {
        this.jda = jda;
        this.systemData = systemData;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("contact") || e.getOptions().isEmpty()) return;

        if (e.getOption("tag") != null && e.getOption("tag").getAsMember() != null && e.getOption("message") != null) {
            sendMessage(e.getOption("tag").getAsMember().getUser(), e.getOption("message").getAsString());
        } else if (e.getOption("tag") == null && e.getOption("message") != null && jda.retrieveUserById(this.systemData.getOwner(e.getGuild().getId())).complete() != null) {
            sendMessage(jda.retrieveUserById(systemData.getOwner(e.getGuild().getId())).complete(), e.getOption("message").getAsString());
        }
        e.replyEmbeds(messenger.getMessageEmbed("sendMessage")).queue();
    }

    /**
     * @param user    The user to send the message.
     * @param message The message to send.
     */
    private void sendMessage(User user, String message) {
        user.openPrivateChannel().flatMap((channel) -> channel.sendMessage(message)).queue();
    }
}
