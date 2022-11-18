package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.moderate.MutedUserManager;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

public class UnMute extends ListenerAdapter {
    private final MutedUserManager mutedUserManager;
    private final SystemData systemData;
    private final Messenger messenger;

    public UnMute(JDA jda, MutedUserManager mutedUserManager, SystemData systemData, Messenger messenger) {
        this.mutedUserManager = mutedUserManager;
        this.systemData = systemData;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("unMute")) return;
        if (!canRunCommand(e)) return;


        long user = e.getOption("member").getAsMentionable().getIdLong();

        if (user != -1L && mutedUserManager.getMutedUsersDataSetForServer(String.valueOf(user)) != null) {
            String reasonToUnmute = e.getOption("reason").getAsString();
            mutedUserManager.unMute(String.valueOf(user), e.getGuild().getId(), reasonToUnmute);
        }

    }

    private boolean canRunCommand(SlashCommandInteractionEvent e) {
        if (!Utils.hasManageChannel(e.getMember())) {
            e.replyEmbeds(messenger.getMessageEmbed("noPermission")).queue();
            return false;
        } else if (e.getOptions().isEmpty() || e.getOptions().size() != 2) {
            e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        } else if (e.getOption("member") == null || e.getOption("reason") == null) {
            e.replyEmbeds(messenger.getMessageEmbed("wrongArgument")).queue();
            return false;
        }
        return true;
    }

}
