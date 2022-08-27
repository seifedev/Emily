package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

public class SetOwner extends ListenerAdapter implements Details {

    private final SystemManager systemData;

    public SetOwner(SystemManager systemData) {
        this.systemData = systemData;
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase(systemData.getCommandPrefix() + "setOwner")) {

            if (message.length != 2 || !Utils.hasAdmin(e.getMember())) return;

            long userId = Utils.getUserId(message[1]);

            if (e.getGuild().retrieveMemberById(parseId(String.valueOf(userId))).complete() != null) {
                e.getGuild().retrieveMemberById(parseId(String.valueOf(userId))).queue(member -> {
                    if (member != null) {
                        systemData.setOwner(String.valueOf(userId));
                        eraseCommand(systemData, e.getMessage());
                    }
                });
            }
        }
    }

    private long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "setOwner <tag user> to set the new user of the bot in a server.";
    }
}
