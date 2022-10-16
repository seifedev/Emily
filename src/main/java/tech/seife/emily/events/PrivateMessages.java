package tech.seife.emily.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.datamanager.data.system.SystemData;

public class PrivateMessages extends ListenerAdapter {

    private final JDA jda;
    private final SystemData systemData;

    public PrivateMessages(SystemData SystemData, JDA jda) {
        this.systemData = SystemData;
        this.jda = jda;
    }

    /**
     * Receives the privates messages send to "Emily" and logs them to the appropriate channels.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) {
            if (jda.getTextChannelById(systemData.getDmChannel(e.getGuild().getId())) != null) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(jda.getSelfUser().getName() + " received the following");
                embedBuilder.setDescription(e.getMessage().getContentRaw());

                jda.getTextChannelById(systemData.getDmChannel(e.getGuild().getId())).sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }
}
