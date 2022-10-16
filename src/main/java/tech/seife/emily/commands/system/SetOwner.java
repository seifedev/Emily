package tech.seife.emily.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;
import tech.seife.emily.utils.Utils;

public class SetOwner extends ListenerAdapter implements Details {

    private final SystemData systemData;

    public SetOwner(SystemData systemData) {
        this.systemData = systemData;
    }


    /**
     * Set's the owner of the bot for the said server.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] message = e.getMessage().getContentRaw().split(" ");

        if (message[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "setOwner")) {

            if (message.length != 2 || !Utils.hasAdmin(e.getMember())) return;

            long userId = Utils.getUserId(message[1]);

            if (e.getGuild().retrieveMemberById(parseId(String.valueOf(userId))).complete() != null) {
                e.getGuild().retrieveMemberById(parseId(String.valueOf(userId))).queue(member -> {
                    if (member != null) {
                        systemData.setOwner(e.getGuild().getId(), String.valueOf(userId));
                        eraseCommand(systemData, e.getMessage(), e.getGuild().getId());

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
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "setOwner <tag user> to set the new user of the bot in a server.";
    }
}
