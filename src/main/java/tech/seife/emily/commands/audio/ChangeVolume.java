package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;

public class ChangeVolume extends ListenerAdapter implements Details {

    private final SystemData systemData;
    private final GuildAudioPlayer guildAudioPlayer;

    public ChangeVolume(SystemData systemData, GuildAudioPlayer guildAudioPlayer) {
        this.systemData = systemData;
        this.guildAudioPlayer = guildAudioPlayer;
    }

    /**
     * it changes the sound volume of the bot, based on the % that the member gave.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        if (!systemData.hasMusicChannel(e.getGuild().getId()) || !systemData.getMusicChannel(e.getGuild().getId()).equals(e.getChannel().getId()))
            return;

        if (e.getMember() == null || e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel())
            return;

        String[] messages = e.getMessage().getContentRaw().split(" ");

        if (messages.length == 2 && messages[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "volume") && getNumber(messages[1]) != -1) {
            if (guildAudioPlayer.dataExists(e.getGuild().getId())) {
                guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().setVolume(getNumber(messages[1]));
                eraseCommand(systemData, e.getMessage(), e.getGuild().getId());
            }

        }

    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "volume, change the percentage of the volume";
    }

    private int getNumber(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
