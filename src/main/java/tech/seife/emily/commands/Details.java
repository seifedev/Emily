package tech.seife.emily.commands;

import net.dv8tion.jda.api.entities.Message;
import tech.seife.emily.datamanager.data.system.SystemManager;

import java.util.concurrent.TimeUnit;

public interface Details {

    String getExplanation();

    default void eraseCommand(SystemManager SystemData, Message message) {
        if (SystemData.getSelfDestructValue()) {
            message.delete().queueAfter(SystemData.getSelfDestructDelay(), TimeUnit.SECONDS);
        }
    }
}
