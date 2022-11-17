package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

public class SetOwner extends ListenerAdapter {

    private final SystemData systemData;
    private final Messenger messenger;

    public SetOwner(SystemData systemData, Messenger messenger) {
        this.systemData = systemData;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("setOwner") || !canRunCommand(e)) return;

        long userId = Utils.getUserId(e.getSubcommandGroup().split(" ")[1]);

        e.getGuild().retrieveMemberById(Utils.parseLong(String.valueOf(userId))).queue(member -> {
            if (member != null) {
                systemData.setOwner(e.getGuild().getId(), String.valueOf(userId));
            }
        });
    }

    private boolean canRunCommand(SlashCommandInteractionEvent e) {
        if (!Utils.hasManageChannel(e.getMember())) {
             e.replyEmbeds(messenger.getMessageEmbed("noPermission")).queue();
            return false;
        } else if (e.getSubcommandGroup() == null || e.getSubcommandGroup().split(" ").length != 1) {
             e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        } else if (Utils.getUserId(e.getSubcommandGroup().split(" ")[1]) == -1 || e.getGuild().retrieveMemberById(Utils.parseLong(String.valueOf(Utils.getUserId(e.getSubcommandGroup().split(" ")[1])))).complete() == null) {
             e.replyEmbeds(messenger.getMessageEmbed("userNotFound")).queue();
            return false;
        }
        return true;
    }

}
