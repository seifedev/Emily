package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

public class Contact extends ListenerAdapter implements Details {
    private final JDA jda;
    private final SystemManager systemData;

    public Contact(JDA jda, SystemManager systemData) {
        this.jda = jda;
        this.systemData = systemData;
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 1) {
            if (message[0].equalsIgnoreCase(this.systemData.getCommandPrefix() + "contact")) {
                this.messageUser(message, e.getTextChannel());
                this.eraseCommand(this.systemData, e.getMessage());
            }

        }
    }

    private void messageUser(String[] arguments, TextChannel channel) {
        if (Utils.getUserId(arguments[1]) != -1L && this.jda.retrieveUserById(Utils.getUserId(arguments[1])) != null) {
            this.sendMessage(2, this.jda.retrieveUserById(Utils.getUserId(arguments[1])).complete(), arguments);
        } else if (this.jda.retrieveUserById(this.systemData.getOwner()).complete() != null) {
            this.sendMessage(1, this.jda.retrieveUserById(this.systemData.getOwner()).complete(), arguments);
        } else {
            this.sendErrorMessage(channel);
        }

    }

    private void sendErrorMessage(TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Failed to send message");
        embedBuilder.setDescription("Make sure the arguments and or owner is correct.");
        embedBuilder.setFooter("Best of luck");
        channel.sendMessageEmbeds(embedBuilder.build(), new MessageEmbed[0]).queue();
    }

    private void sendMessage(int startFrom, User user, String[] arguments) {
        String message = this.getMessage(startFrom, arguments);
        user.openPrivateChannel().flatMap((channel) -> {
            return channel.sendMessage(message);
        }).queue();
    }

    private String getMessage(int startFrom, String[] arguments) {
        StringBuilder message = new StringBuilder();

        for (int i = startFrom; i < arguments.length; ++i) {
            message.append(arguments[i]).append(" ");
        }

        return message.toString();
    }

    public String getExplanation() {
        return this.systemData.getCommandPrefix() + "contact to contact the current owner of the bot in this server.";
    }
}
