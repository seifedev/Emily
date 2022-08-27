package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.mute.MutedUserManager;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

public class UnMute extends ListenerAdapter implements Details {
    private final JDA jda;
    private final MutedUserManager mutedUserManager;
    private final SystemManager systemData;

    public UnMute(JDA jda, MutedUserManager mutedUserManager, SystemManager systemData) {
        this.jda = jda;
        this.mutedUserManager = mutedUserManager;
        this.systemData = systemData;
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String[] messages = e.getMessage().getContentRaw().split(" ");
        if (messages.length > 2 && Utils.hasAdmin(e.getMember())) {
            if (messages[0].equalsIgnoreCase(this.systemData.getCommandPrefix() + "unmute")) {
                long user = Utils.getUserId(messages[1]);
                if (user != -1L && this.mutedUserManager.getMutedUsersDataSet(String.valueOf(user)) != null) {
                    String reasonToUnmute = this.getReasonToUnmute(messages);
                    this.mutedUserManager.unMute(String.valueOf(user), reasonToUnmute);
                    this.eraseCommand(this.systemData, e.getMessage());
                }
            }

        }
    }

    private String getReasonToUnmute(String[] messages) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < messages.length; ++i) {
            sb.append(messages[i]).append(" ");
        }

        return sb.toString();
    }

    public String getExplanation() {
        return this.systemData.getCommandPrefix() + "unmute <tag a player> <reason> to unmute a muted user for the following <reason>.";
    }
}
