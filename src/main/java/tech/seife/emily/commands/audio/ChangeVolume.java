package tech.seife.emily.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class ChangeVolume extends ListenerAdapter implements Details {

    private final SystemManager systemData;
    private final AudioPlayer audioPlayer;

    public ChangeVolume(SystemManager systemData, AudioPlayer audioPlayer) {
        this.systemData = systemData;
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!systemData.getMusicChannel().equals(systemData.getMusicChannel())) return;

        String[] messages = e.getMessage().getContentRaw().split(" ");

        if (e.getMember() == null || e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel())
            return;

        if (messages.length == 2 && messages[0].equalsIgnoreCase("volume") && getNumber(messages[1]) != -1) {
            audioPlayer.setVolume(getNumber(messages[1]));
            eraseCommand(systemData, e.getMessage());
        }

    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "volume, change the percentage of the volume";
    }

    private int getNumber(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
