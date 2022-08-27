package tech.seife.emily.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.datamanager.data.mute.MutedUserManager;

public class AutomaticUnmute extends ListenerAdapter {

    private final MutedUserManager mutedUserManager;

    public AutomaticUnmute(MutedUserManager mutedUserManager) {
        this.mutedUserManager = mutedUserManager;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (mutedUserManager.getMutedUsersDataSet(event.getAuthor().getId()) != null) {
            if (mutedUserManager.canUnmuteUser(event.getAuthor().getId())) {
                mutedUserManager.unMute(event.getAuthor().getId(), "Enough time passed. The mute has been removed by the system.");
            } else {
                event.getMessage().delete().queue();
            }
        }
    }
}
