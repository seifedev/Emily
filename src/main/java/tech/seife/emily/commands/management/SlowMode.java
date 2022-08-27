package tech.seife.emily.commands.management;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

public class SlowMode extends ListenerAdapter implements Details {
    private final SystemManager systemData;

    public SlowMode(SystemManager systemData) {
        this.systemData = systemData;
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        String[] messages = e.getMessage().getContentRaw().split(" ");
        if (messages.length == 2 && Utils.hasManageChannel(e.getMember())) {
            if (this.validArguments(messages[0], messages[1]) && e.getChannel().getType().equals(ChannelType.TEXT)) {
                TextChannel textChannel = e.getTextChannel();
                textChannel.getManager().setSlowmode(this.parseNumber(messages[1])).queue();
                this.eraseCommand(this.systemData, e.getMessage());
            }

        }
    }

    private boolean validArguments(String command, String numberToParse) {
        if (command.equalsIgnoreCase(this.systemData.getCommandPrefix() + "slowMo")) {
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

    public String getExplanation() {
        return this.systemData.getCommandPrefix() + "slowMo <time> enable the slow mode in a channel";
    }
}
