package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;

public class SetMusicChannel extends ListenerAdapter implements Details {

    private final SystemData systemData;

    public SetMusicChannel(SystemData SystemData) {
        this.systemData = SystemData;
    }

    /**
     * There can be only one music channel per time.
     * This class can define the music channel or change it if there is one already.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length == 1 && message[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "setMusicChannel")) {
            systemData.setMusicChannel(e.getGuild().getId(), e.getChannel().getId());
            eraseCommand(systemData, e.getMessage(), e.getGuild().getId());
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "Sets a channel to play music";
    }
}
