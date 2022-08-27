package tech.seife.emily.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
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


    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent e) {
        if (queueManager.getEmbed(e.getUserId(), e.getMessageId()) != null) {
            switch (e.getReactionEmote().getAsCodepoints()) {
                case "U+274c" -> {
                    e.getChannel().deleteMessageById(e.getMessageId()).queue();
                    queueManager.clearUserPage(e.getUserId());
                }
                case "U+23ed" -> {
                    if ((queueManager.getAmountOfSongs() / 10) > queueManager.getPage(e.getUserId())) {
                        queueManager.setUserPage(e.getUserId(), queueManager.getPage(e.getUserId()) + 1);
                        editEmbed(e.getTextChannel(), e.getUserId(), e.getMessageId());
                    }
                }
                case "U+23ee" -> {
                    if (queueManager.getPage(e.getUserId()) > 0) {
                        queueManager.setUserPage(e.getUserId(), queueManager.getPage(e.getUserId()) - 1);
                        editEmbed(e.getTextChannel(), e.getUserId(), e.getMessageId());
                    }
                }
            }
        }
    }

    private void editEmbed(TextChannel channel, String userId, String messageId) {

        EmbedBuilder embedBuilder = queueManager.getEmbed(userId, messageId);

        channel.retrieveMessageById(messageId)
                .queue(message -> {
                    message.editMessageEmbeds(queueManager.getEmbedQueue((queueManager.getPage(userId)) * 10L, queueManager.getPage(userId), jda, embedBuilder).build())
                            .queue(message1 -> {
                                queueManager.addQueueManager(userId, messageId, embedBuilder);
                                message.clearReactions().queue();
                                queueManager.addReactions(message1);
                            });
                });
    }

}
