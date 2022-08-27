package tech.seife.emily.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.audio.AudioPlayerSendHandler;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemManager;

public class Summon extends ListenerAdapter implements Details {

    private final SystemManager systemData;
    private final AudioPlayer audioPlayer;

    public Summon(SystemManager systemData, AudioPlayer audioPlayer) {
        this.systemData = systemData;
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!systemData.getMusicChannel().equals(systemData.getMusicChannel())) return;
        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix() + "summon")) {
            if (e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) {
                e.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            } else {
                AudioManager audioManager = e.getGuild().getAudioManager();
                AudioChannel channel = e.getMember().getVoiceState().getChannel();
                audioManager.openAudioConnection(channel);

                audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
                audioManager.setSelfDeafened(true);
                audioManager.setSelfMuted(false);
                eraseCommand(systemData, e.getMessage());
            }
        }
    }

    @Override
    public String getExplanation() {
        return systemData.getCommandPrefix() + "summon to put the bot into the voice channel.";
    }
}
