package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.moderate.MutedUserManager;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

import java.time.LocalDateTime;

public class Mute extends ListenerAdapter {
    private final MutedUserManager mutedUserManager;
    private final SystemData systemData;
    private final Messenger messenger;

    public Mute(MutedUserManager mutedUserManager, SystemData systemData, Messenger messenger) {
        this.mutedUserManager = mutedUserManager;
        this.systemData = systemData;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("mute") || !canRunCommand(e)) return;

        long userId = e.getOption("member").getAsMentionable().getIdLong();

        LocalDateTime releaseDate = getReleaseDate(e.getOption("duration").getAsString());
        String muteReason = e.getOption("reason").getAsString();
        mutedUserManager.muteUser(String.valueOf(userId), e.getGuild().getId(), LocalDateTime.now(), releaseDate, muteReason);
    }

    private boolean canRunCommand(SlashCommandInteractionEvent e) {
        if (!Utils.hasAdmin(e.getMember())) {
             e.replyEmbeds(messenger.getMessageEmbed("noPermission")).queue();
            return false;
        } else if (e.getOptions().isEmpty() || e.getOptions().size() < 3) {
             e.replyEmbeds(messenger.getMessageEmbed("wrongAmountOfOptions")).queue();
            return false;
        } else if (e.getOption("member") == null) {
             e.replyEmbeds(messenger.getMessageEmbed("wrongArgument")).queue();
            return false;
        } else if (mutedUserManager.getMutedUsersDataSetForServer(e.getOption("member").getName()) != null) {
             e.replyEmbeds(messenger.getMessageEmbed("userNotFound")).queue();
            return false;
        }
        return true;
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
}
