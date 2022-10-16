package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;
import tech.seife.emily.utils.Utils;

public class SetPrefix extends ListenerAdapter implements Details {

    private final SystemData systemData;
    private final JDA jda;

    public SetPrefix(JDA jda, SystemData systemData) {
        this.jda = jda;
        this.systemData = systemData;
    }

    /**
     * It changes the prefix of the commands.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length != 2 || !Utils.hasAdmin(e.getMember())) return;

        String oldPrefix = systemData.getCommandPrefix(e.getGuild().getId());

        if (message[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "setPrefix")) {
            systemData.setNewPrefix(e.getGuild().getId(),message[1]);

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setAuthor(jda.getSelfUser().getName());
            embedBuilder.setTitle("The prefix has been changed.");
            embedBuilder.setDescription("The old prefix is: " + oldPrefix + " the new prefix is: " + systemData.getCommandPrefix(e.getGuild().getId()));

            e.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                            eraseCommand(systemData, e.getMessage(), e.getGuild().getId());


        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "setPrefix <prefix> to make a symbol the new prefix of the commands.";
    }
}
