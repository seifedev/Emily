package tech.seife.emily.scheduler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

public class BotStatus implements Runnable {

    private final JDA jda;

    public BotStatus(JDA jda) {
        this.jda = jda;
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
    }


    /**
     * It goes through all the servers that the bot has been installed and counts the members.
     */
    @Override
    public void run() {
        int members = 0;

        for (Guild guild : jda.getGuilds()) {
            members += guild.retrieveMetaData().map(Guild.MetaData::getApproximateMembers).complete();
        }

        jda.getPresence().setActivity(Activity.watching(members + " users"));
    }
}
