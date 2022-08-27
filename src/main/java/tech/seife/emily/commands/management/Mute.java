package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.mute.MutedUserManager;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

import java.time.LocalDateTime;

public class Mute extends ListenerAdapter implements Details {
    private final MutedUserManager mutedUserManager;
    private final SystemManager systemData;

    public Mute(MutedUserManager mutedUserManager, SystemManager systemData) {
        this.mutedUserManager = mutedUserManager;
        this.systemData = systemData;
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String[] messages = e.getMessage().getContentRaw().split(" ");
        if (messages.length > 3 && Utils.hasAdmin(e.getMember())) {
            if (messages[0].equalsIgnoreCase(this.systemData.getCommandPrefix() + "mute")) {
                long userId = Utils.getUserId(messages[1]);
                if (userId != -1L && this.mutedUserManager.getMutedUsersDataSet(String.valueOf(userId)) == null) {
                    LocalDateTime releaseDate = this.getReleaseDate(messages[2]);
                    String muteReason = this.getReason(messages);
                    this.mutedUserManager.muteUser(String.valueOf(userId), releaseDate, muteReason);
                    this.eraseCommand(this.systemData, e.getMessage());
                }
            }

        }
    }

    private String getReason(String[] message) {
        StringBuilder sb = new StringBuilder();

        for (int i = 3; i < message.length; ++i) {
            sb.append(message[i]).append(" ");
        }

        return sb.toString();
    }

    private LocalDateTime getReleaseDate(String date) {
        if (date.contains("seconds")) {
            date = date.replace("seconds", "");
            return LocalDateTime.now().plusSeconds(this.parseNumber(date));
        } else if (date.contains("minutes")) {
            date = date.replace("minutes", "");
            return LocalDateTime.now().plusMinutes(this.parseNumber(date));
        } else if (date.contains("hours")) {
            date = date.replace("hours", "");
            return LocalDateTime.now().plusHours(this.parseNumber(date));
        } else if (date.contains("days")) {
            date = date.replace("days", "");
            return LocalDateTime.now().plusDays(this.parseNumber(date));
        } else if (date.contains("months")) {
            date = date.replace("months", "");
            return LocalDateTime.now().plusMonths(this.parseNumber(date));
        } else if (date.contains("years")) {
            date = date.replace("years", "");
            return LocalDateTime.now().plusYears(this.parseNumber(date));
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

    public String getExplanation() {
        return this.systemData.getCommandPrefix() + "mute <tag a player> <time> <reason> to mute a user for a certain amount of <time> and for the following <reason>.";
    }
}
