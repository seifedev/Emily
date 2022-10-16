package tech.seife.emily.commands;

import net.dv8tion.jda.api.entities.Message;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;

import java.util.concurrent.TimeUnit;

public interface Details {


    String getExplanation(String serverId);

    default void eraseCommand(SystemData systemData, Message message, String serverId) {
        if (systemData.getSelfDestructValue(serverId)) {
            message.delete().queueAfter(systemData.getSelfDestructDelay(serverId), TimeUnit.SECONDS);
        }
    }
}
