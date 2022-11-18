package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

public class SlowMode extends ListenerAdapter {
    private final SystemData systemData;
    private final Messenger messenger;

    public SlowMode(SystemData systemData, Messenger messenger) {
        this.systemData = systemData;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("slowMode")) return;
        if (!canRunCommand(e)) return;


        TextChannel textChannel = e.getChannel().asTextChannel();
        textChannel.getManager().setSlowmode(e.getOption("delay").getAsInt()).queue();

    }

    private boolean canRunCommand(SlashCommandInteractionEvent e) {
        if (!Utils.hasManageChannel(e.getMember())) {
             e.replyEmbeds(messenger.getMessageEmbed("noPermission")).queue();
            return false;
        } else if (e.getOptions().isEmpty() || e.getOption("delay") == null) {
             e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        }
        return true;
    }
}
