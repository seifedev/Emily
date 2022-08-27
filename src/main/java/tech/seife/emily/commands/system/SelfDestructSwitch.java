package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

public class SelfDestructSwitch extends ListenerAdapter implements Details {

    private final SystemManager systemData;

    public SelfDestructSwitch(SystemManager systemData) {
        this.systemData = systemData;
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix() + "selfDestruct")) {
            if (Utils.hasAdmin(e.getMember())) {
                systemData.changeSelfDestruct();
                eraseCommand(systemData, e.getMessage());

            }
        }
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "selfDestruct, enable/disable the function for the commands to be deleted after a certain amount of time";
    }
}
