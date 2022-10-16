package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.utils.Utils;

public class SkipPartsOfMusic extends ListenerAdapter implements Details {

    private final SystemData systemData;
    private final GuildAudioPlayer guildAudioPlayer;

    public SkipPartsOfMusic(SystemData systemData, GuildAudioPlayer guildAudioPlayer) {
        this.systemData = systemData;
        this.guildAudioPlayer = guildAudioPlayer;
    }

    /**
     * Skip certain parts of the current song that is being played.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {

        if (!e.isFromGuild()) return;

        if (!systemData.hasMusicChannel(e.getGuild().getId()) || !systemData.getMusicChannel(e.getGuild().getId()).equals(e.getMessage().getChannel().getId())) return;

        String[] messages = e.getMessage().getContentRaw().split(" ");

        if (messages.length == 2 && messages[0].equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "seek")) {
            long number = Utils.parseLong(messages[1]) * 60L;


            if (number != -1 && guildAudioPlayer.dataExists(e.getGuild().getId())) {
                guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().getPlayingTrack().setPosition((guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().getPlayingTrack().getPosition() + number));
                eraseCommand(systemData, e.getMessage(), e.getGuild().getId());
            }
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "seek -+ <int> to go forwards or backwards on the current playing song.";
    }
}
