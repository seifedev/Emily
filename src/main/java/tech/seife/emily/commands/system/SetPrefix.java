package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

public class SetPrefix extends ListenerAdapter implements Details {

    private final SystemManager systemData;
    private final JDA jda;

    public SetPrefix(JDA jda, SystemManager systemData) {
        this.jda = jda;
        this.systemData = systemData;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length != 2 || !Utils.hasAdmin(e.getMember())) return;

        String oldPrefix = systemData.getCommandPrefix();
        if (message[0].equalsIgnoreCase(systemData.getCommandPrefix() + "setPrefix")) {
            systemData.setNewPrefix(message[1]);

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setAuthor(jda.getSelfUser().getName());
            embedBuilder.setTitle("The prefix has been changed.");
            embedBuilder.setDescription("The old prefix is: " + oldPrefix + " the new prefix is: " + systemData.getCommandPrefix());

            e.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            eraseCommand(systemData, e.getMessage());

        }
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "setPrefix <prefix> to make a symbol the new prefix of the commands.";
    }
}
