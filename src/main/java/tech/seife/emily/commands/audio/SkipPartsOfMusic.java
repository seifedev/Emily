package tech.seife.emily.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;
import tech.seife.emily.utils.Utils;

public class SkipPartsOfMusic extends ListenerAdapter implements Details {

    private final SystemManager systemData;
    private final AudioPlayer audioPlayer;

    public SkipPartsOfMusic(SystemManager systemData, AudioPlayer audioPlayer) {
        this.systemData = systemData;
        this.audioPlayer = audioPlayer;

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!systemData.getMusicChannel().equals(systemData.getMusicChannel())) return;
        String[] messages = e.getMessage().getContentRaw().split(" ");

        if (messages.length == 2 && messages[0].equalsIgnoreCase(systemData.getCommandPrefix() + "seek")) {
            long number = Utils.parseLong(messages[1]) * 60L;

            if (number != -1) {
                audioPlayer.getPlayingTrack().setPosition(audioPlayer.getPlayingTrack().getPosition() + number);
                eraseCommand(systemData, e.getMessage());
            }
        }
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "seek -+ <int> to go forwards or backwards on the current playing song.";
    }
}
