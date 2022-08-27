package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class ShowQueue extends ListenerAdapter implements Details {

    private final SystemManager systemData;
    private final QueueManager queueManager;
    private final JDA jda;

    public ShowQueue(SystemManager systemData, QueueManager queueManager, JDA jda) {
        this.systemData = systemData;
        this.queueManager = queueManager;
        this.jda = jda;
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix() + "queue")) {
            EmbedBuilder embedBuilder = queueManager.getEmbedQueue(0, 0, jda, new EmbedBuilder());

            e.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
                queueManager.addReactions(message);
                queueManager.addQueueManager(e.getMember().getUser().getId(), message.getId(), embedBuilder);
                queueManager.setUserPage(e.getAuthor().getId(), 0);
                eraseCommand(systemData, e.getMessage());
            });

        }
    }


    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "queue, to view the all songs that are in the queue";
    }
}
