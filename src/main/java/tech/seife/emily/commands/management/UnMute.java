package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.moderate.MutedUserManager;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

public class UnMute extends ListenerAdapter implements Details {
    private final JDA jda;
    private final MutedUserManager mutedUserManager;
    private final SystemData systemData;

    public UnMute(JDA jda, MutedUserManager mutedUserManager, SystemData systemData) {
        this.jda = jda;
        this.mutedUserManager = mutedUserManager;
        this.systemData = systemData;
    }

    /**
     * It attempts to un-mute a member.
     */
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] messages = e.getMessage().getContentRaw().split(" ");
        if (messages.length > 2 && Utils.hasAdmin(e.getMember())) {
            if (messages[0].equalsIgnoreCase(this.systemData.getCommandPrefix(e.getGuild().getId()) + "unmute")) {
                long user = Utils.getUserId(messages[1]);
                if (user != -1L && this.mutedUserManager.getMutedUsersDataSetForServer(String.valueOf(user)) != null) {
                    String reasonToUnmute = this.getReasonToUnmute(messages);
                    mutedUserManager.unMute(String.valueOf(user), e.getGuild().getId(), reasonToUnmute);
                    eraseCommand(systemData, e.getMessage(), e.getGuild().getId());
                }
            }

        }
    }

    /**
     * The reason will be saved in the database
     * @param input Contains the reason as to why the member should be un muted
     * @return the reason to remove the mute.
     */
    private String getReasonToUnmute(String[] input) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < input.length; ++i) {
            sb.append(input[i]).append(" ");
        }

        return sb.toString();
    }

    public String getExplanation(String serverId) {
        return this.systemData.getCommandPrefix(serverId) + "unmute <tag a player> <reason> to unmute a muted user for the following <reason>.";
    }
}
