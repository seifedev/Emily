package tech.seife.emily.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class PrivateMessages extends ListenerAdapter {

    private final JDA jda;
    private final SystemManager systemData;

    public PrivateMessages(SystemManager SystemData, JDA jda) {
        this.systemData = SystemData;
        this.jda = jda;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try {
            if (event.getPrivateChannel() != null) {
                if (jda.getTextChannelById(systemData.getDmChannel()) != null) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle(jda.getSelfUser().getName() + " received the following");
                    embedBuilder.setDescription(event.getMessage().getContentRaw());

                    jda.getTextChannelById(systemData.getDmChannel()).sendMessageEmbeds(embedBuilder.build()).queue();
                }
            }
        } catch (IllegalStateException e) {
        }
    }
}
