package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;
import tech.seife.emily.utils.Utils;

public class SlowMode extends ListenerAdapter implements Details {
    private final SystemData systemData;

    public SlowMode(SystemData systemData) {
        this.systemData = systemData;
    }

    /**
     *
     * It enables the slow mod for X amounts of seconds or disables it if it's enabled.
     */
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        String[] messages = e.getMessage().getContentRaw().split(" ");
        if (messages.length == 2 && Utils.hasManageChannel(e.getMember())) {
            if (validArguments(messages[0], messages[1], e.getGuild().getId()) && e.getChannel().getType().equals(ChannelType.TEXT)) {
                TextChannel textChannel = e.getTextChannel();
                textChannel.getManager().setSlowmode(this.parseNumber(messages[1])).queue();
                eraseCommand(systemData, e.getMessage(), e.getGuild().getId());
            }

        }
    }

    /**
     *
     * It validates if the input & their arguments of the input are correct
     * @param command the input of the member.
     * @param numberToParse the interval between when a member can send a message.
     * @param serverId The ID of the server.
     * @return True if the command is valid, false otherwise.
     */
    private boolean validArguments(String command, String numberToParse, String serverId) {
        if (command.equalsIgnoreCase(this.systemData.getCommandPrefix(serverId) + "slowMo")) {
            return this.parseNumber(numberToParse) != -1;
        } else {
            return false;
        }
    }

    private int parseNumber(String numberToParse) {
        try {
            return Integer.parseInt(numberToParse);
        } catch (NumberFormatException var3) {
            return -1;
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return this.systemData.getCommandPrefix(serverId) + "slowMo <time> enable the slow mode in a channel";
    }
}
