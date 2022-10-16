package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;
import tech.seife.emily.utils.Utils;

public class SelfDestructSwitch extends ListenerAdapter implements Details {

    private final SystemData systemData;

    public SelfDestructSwitch(SystemData systemData) {
        this.systemData = systemData;
    }


    /**
     * Enable/Disable the self destruction of the commands in the chat.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {

        if (!e.isFromGuild()) return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "selfDestruct")) {
            if (Utils.hasAdmin(e.getMember())) {
                systemData.changeSelfDestruct(e.getGuild().getId());
                                eraseCommand(systemData, e.getMessage(), e.getGuild().getId());


            }
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "selfDestruct, enable/disable the function for the commands to be deleted after a certain amount of time";
    }
}
