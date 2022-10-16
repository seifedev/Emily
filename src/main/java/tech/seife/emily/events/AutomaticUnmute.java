package tech.seife.emily.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.datamanager.data.moderate.MutedUserManager;

public class AutomaticUnmute extends ListenerAdapter {

    private final MutedUserManager mutedUserManager;

    public AutomaticUnmute(MutedUserManager mutedUserManager) {
        this.mutedUserManager = mutedUserManager;
    }

    /**
     * Removes the mute of the user if it's paste the "punishment" date.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        if (mutedUserManager.getMutedUsersDataSetForServer(e.getGuild().getId()) != null) {
            if (mutedUserManager.canUnmuteUser(e.getGuild().getId(), e.getAuthor().getId())) {
                mutedUserManager.unMute(e.getAuthor().getId(), e.getGuild().getId(), "Enough time passed. The mute has been removed by the system.");
            } else {
                e.getMessage().delete().queue();
            }
        }
    }
}
