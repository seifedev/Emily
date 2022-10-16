package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.moderate.MutedUserManager;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

import java.time.LocalDateTime;

public class Mute extends ListenerAdapter implements Details {
    private final MutedUserManager mutedUserManager;
    private final SystemData systemData;

    public Mute(MutedUserManager mutedUserManager, SystemData systemData) {
        this.mutedUserManager = mutedUserManager;
        this.systemData = systemData;
    }

    /**
     * Attempts to mute a member.
     */
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;
        String[] messages = e.getMessage().getContentRaw().split(" ");
        if (messages.length > 3 && Utils.hasAdmin(e.getMember())) {
            if (messages[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "mute")) {
                long userId = Utils.getUserId(messages[1]);
                if (userId != -1L && this.mutedUserManager.getMutedUsersDataSetForServer(String.valueOf(userId)) == null) {
                    LocalDateTime releaseDate = this.getReleaseDate(messages[2]);
                    String muteReason = this.getReason(messages);
                    mutedUserManager.muteUser(String.valueOf(userId), e.getGuild().getId(), LocalDateTime.now(), releaseDate, muteReason);
                    eraseCommand(systemData, e.getMessage(), e.getGuild().getId());
                }
            }

        }
    }

    /**
     * @param input It's input message of the member who initiated the mute
     * @return The reason as to why the member will be muted.
     */
    private String getReason(String[] input) {
        StringBuilder sb = new StringBuilder();

        for (int i = 3; i < input.length; ++i) {
            sb.append(input[i]).append(" ");
        }

        return sb.toString();
    }

    /**
     * @param muteAmount for how long will the user be muted
     * @return A date in the future with the amount of time the user should not be able to chat.
     */
    private LocalDateTime getReleaseDate(String muteAmount) {
        if (muteAmount.contains("seconds")) {
            muteAmount = muteAmount.replace("seconds", "");
            return LocalDateTime.now().plusSeconds(this.parseNumber(muteAmount));
        } else if (muteAmount.contains("minutes")) {
            muteAmount = muteAmount.replace("minutes", "");
            return LocalDateTime.now().plusMinutes(this.parseNumber(muteAmount));
        } else if (muteAmount.contains("hours")) {
            muteAmount = muteAmount.replace("hours", "");
            return LocalDateTime.now().plusHours(this.parseNumber(muteAmount));
        } else if (muteAmount.contains("days")) {
            muteAmount = muteAmount.replace("days", "");
            return LocalDateTime.now().plusDays(this.parseNumber(muteAmount));
        } else if (muteAmount.contains("months")) {
            muteAmount = muteAmount.replace("months", "");
            return LocalDateTime.now().plusMonths(this.parseNumber(muteAmount));
        } else if (muteAmount.contains("years")) {
            muteAmount = muteAmount.replace("years", "");
            return LocalDateTime.now().plusYears(this.parseNumber(muteAmount));
        } else {
            return LocalDateTime.now();
        }
    }

    private long parseNumber(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException var3) {
            return -1L;
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return this.systemData.getCommandPrefix(serverId) + "mute <tag a player> <time> <reason> to mute a user for a certain amount of <time> and for the following <reason>.";
    }
}
