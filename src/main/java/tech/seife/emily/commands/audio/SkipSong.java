package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;

public class SkipSong extends ListenerAdapter implements Details {

    private final SystemData systemData;
    private final GuildAudioPlayer guildAudioPlayer;

    public SkipSong(SystemData systemData, GuildAudioPlayer guildAudioPlayer) {
        this.systemData = systemData;
        this.guildAudioPlayer = guildAudioPlayer;

    }

    /**
     * Directly skips a song if there is a next one in the queue,
     * Otherwise it stops playing music.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        if (e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) return;

        if (!systemData.hasMusicChannel(e.getGuild().getId()) || !systemData.getMusicChannel(e.getGuild().getId()).equals(e.getChannel().getId()))
            return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "skip")) {
            if (guildAudioPlayer.dataExists(e.getGuild().getId()) && guildAudioPlayer.getAudioData(e.getGuild().getId()) != null) {
                guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().getPlayingTrack().setPosition(guildAudioPlayer.getAudioData(e.getGuild().getId()).audioPlayer().getPlayingTrack().getInfo().length);
                eraseCommand(systemData, e.getMessage(), e.getGuild().getId());
            }
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "skip to skip the current song.";
    }
}
