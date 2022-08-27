package tech.seife.emily.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class SkipSong extends ListenerAdapter implements Details {

    private final SystemManager systemData;
    private final AudioPlayer audioPlayer;

    public SkipSong(SystemManager systemData, AudioPlayer audioPlayer) {
        this.systemData = systemData;
        this.audioPlayer = audioPlayer;

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!systemData.getMusicChannel().equals(systemData.getMusicChannel())) return;
        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix() + "skip")) {
            if (e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) {
                audioPlayer.getPlayingTrack().setPosition(audioPlayer.getPlayingTrack().getInfo().length);
                eraseCommand(systemData, e.getMessage());
            }
        }
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "skip to skip the current song.";
    }
}
