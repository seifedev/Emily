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
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;
import tech.seife.emily.utils.Utils;

public class Contact extends ListenerAdapter implements Details {
    private final JDA jda;
    private final SystemData systemData;

    public Contact(JDA jda, SystemData systemData) {
        this.jda = jda;
        this.systemData = systemData;
    }

    /**
     * The bot will send a message to a tagged member or the owner if it has been set
     */
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] message = e.getMessage().getContentRaw().split(" ");
        if (message.length > 1) {
            if (message[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "contact")) {
                messageUser(message, e.getTextChannel(), e.getGuild().getId());
                eraseCommand(systemData, e.getMessage(), e.getGuild().getId());

            }

        }
    }

    /**
     * You can either contact the owner or someone you tagged.
     *
     * @param arguments The input of the member who run the contact command.
     * @param channel   The channel that the command was run.
     * @param serverId  The server ID.
     */
    private void messageUser(String[] arguments, TextChannel channel, String serverId) {
        if (Utils.getUserId(arguments[1]) != -1L && this.jda.retrieveUserById(Utils.getUserId(arguments[1])) != null) {
            this.sendMessage(2, this.jda.retrieveUserById(Utils.getUserId(arguments[1])).complete(), arguments);
        } else if (this.jda.retrieveUserById(this.systemData.getOwner(serverId)).complete() != null) {
            this.sendMessage(1, this.jda.retrieveUserById(this.systemData.getOwner(serverId)).complete(), arguments);
        } else {
            this.sendErrorMessage(channel);
        }

    }

    /**
     * Sends an error message in case the command failed, be it invalid tag or that the owner hasn't been set.
     *
     * @param channel The channel to send the message.
     */
    private void sendErrorMessage(TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Failed to send message");
        embedBuilder.setDescription("Make sure the arguments and or owner is correct.");
        embedBuilder.setFooter("Best of luck");
        channel.sendMessageEmbeds(embedBuilder.build(), new MessageEmbed[0]).queue();
    }

    /**
     * @param startFrom From which word in the input to start processing the message
     * @param user      The user to send the message
     * @param arguments The message.
     */
    private void sendMessage(int startFrom, User user, String[] arguments) {
        String message = this.getMessage(startFrom, arguments);
        user.openPrivateChannel().flatMap((channel) -> channel.sendMessage(message)).queue();
    }

    /**
     * It basically strips down the message, depending if the member has tagged someone or not.
     *
     * @param startFrom From which word in the input to start processing the message
     * @param arguments The message.
     * @return
     */
    private String getMessage(int startFrom, String[] arguments) {
        StringBuilder message = new StringBuilder();

        for (int i = startFrom; i < arguments.length; ++i) {
            message.append(arguments[i]).append(" ");
        }

        return message.toString();
    }

    public String getExplanation(String serverId) {
        return this.systemData.getCommandPrefix(serverId) + "contact to contact the current owner of the bot in this server.";
    }
}
