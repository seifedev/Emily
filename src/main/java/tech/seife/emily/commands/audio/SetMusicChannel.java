package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class SetMusicChannel extends ListenerAdapter implements Details {

    private final SystemManager SystemData;

    public SetMusicChannel(SystemManager SystemData) {
        this.SystemData = SystemData;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!SystemData.getMusicChannel().equals(SystemData.getMusicChannel())) return;

        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message.length == 1 && message[0].equalsIgnoreCase(SystemData.getCommandPrefix() + "setMusicChannel")) {
            SystemData.setMusicChannel(e.getChannel().getId());
            eraseCommand(SystemData, e.getMessage());
        }
    }

    @Override
    public String getExplanation() {
        return SystemData.getCommandPrefix() + "";
    }
}
