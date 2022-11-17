package tech.seife.emily.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.datamanager.data.audio.QueueManager;

public class QueueReactions extends ListenerAdapter {

    private final JDA jda;
    private final QueueManager queueManager;

    public QueueReactions(JDA jda, QueueManager queueManager) {
        this.jda = jda;
        this.queueManager = queueManager;
    }


    /**
     * Based on which emoji was clicked they will be executing certain.
     */
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent e) {
        if (queueManager.getEmbed(e.getUserId(), e.getMessageId()) != null) {

            UnicodeEmoji unicodeEmoji = e.getReaction().getEmoji().asUnicode();
            if ("U+274c".equals(unicodeEmoji)) {
                e.getChannel().deleteMessageById(e.getMessageId()).queue();
                queueManager.clearUserPage(e.getUserId());
            } else if ("U+23ed".equals(unicodeEmoji)) {
                if ((queueManager.getAmountOfRemainingSongs(e.getGuild().getId()) / 10) > queueManager.getPage(e.getUserId())) {
                    queueManager.setUserPage(e.getUserId(), queueManager.getPage(e.getUserId()) + 1);
                    editEmbed(e.getChannel().asTextChannel(), e.getUserId(), e.getMessageId(), e.getGuild().getId());
                }
            } else if ("U+23ee".equals(unicodeEmoji)) {
                if (queueManager.getPage(e.getUserId()) > 0) {
                    queueManager.setUserPage(e.getUserId(), queueManager.getPage(e.getUserId()) - 1);
                    editEmbed(e.getChannel().asTextChannel(), e.getUserId(), e.getMessageId(), e.getGuild().getId());
                }
            }
        }
    }

    /**
     * Adds the appropriate emojis to the queue embed message.
     *
     * @param channel   The channel to edit the embed message, usually in the voice chat channel.
     * @param userId    Who sent the request to view the queue and who can react on the embed.
     * @param messageId The ID of the embed message to add the reactions.
     * @param serverId  The id of the server
     */
    private void editEmbed(TextChannel channel, String userId, String messageId, String serverId) {

        EmbedBuilder embedBuilder = queueManager.getEmbed(userId, messageId);

        channel.retrieveMessageById(messageId).queue(message -> {
            message.editMessageEmbeds(queueManager.getEmbedQueue((queueManager.getPage(userId)) * 10L, queueManager.getPage(userId), jda, embedBuilder, serverId).build()).queue(message1 -> {
                queueManager.addQueueManager(userId, messageId, embedBuilder);
                message.clearReactions().queue();
                queueManager.addReactions(message1);
            });
        });
    }

}
