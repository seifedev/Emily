package tech.seife.emily.commands.entertainment;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.system.SystemData;

public class BotTalk extends ListenerAdapter {

    private final SystemData systemData;
    private final Messenger messenger;

    public BotTalk(SystemData systemData, Messenger messenger) {
        this.systemData = systemData;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("talk") || canRunCommand(e))

            e.getChannel().sendMessage(e.getOption("message").getAsString()).queue();
    }

    private boolean canRunCommand(SlashCommandInteractionEvent e) {
        if (e.getOptions().isEmpty()) {
            e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        }
        return false;
    }

    /**
     * @param arguments What the bot should say
     * @return Returns the message that the bot should send
     */
    private String getMessage(String[] arguments) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < arguments.length; i++) {
            sb.append(arguments[i]).append(" ");
        }
        return sb.toString();
    }
}
