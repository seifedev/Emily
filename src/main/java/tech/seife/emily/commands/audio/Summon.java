package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.audiologic.AudioPlayerSendHandler;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.commands.Details;
import tech.seife.emily.datamanager.data.system.SystemData;

public class Summon extends ListenerAdapter implements Details {

    private final SystemData systemData;
    private final GuildAudioPlayer guildAudioPlayer;

    public Summon(SystemData systemData, GuildAudioPlayer guildAudioPlayer) {
        this.systemData = systemData;
        this.guildAudioPlayer = guildAudioPlayer;
    }


    /**
     * Summons the bot into the channel, usually to play music.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;

        if (!systemData.hasMusicChannel(e.getGuild().getId()) || !systemData.getMusicChannel(e.getGuild().getId()).equals(e.getChannel().getId()))
            return;

        if (e.getMessage().getContentRaw().equalsIgnoreCase(systemData.getCommandPrefix(e.getGuild().getId()) + "summon")) {
            if (e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) {
                e.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            } else {
                AudioManager audioManager = e.getGuild().getAudioManager();
                AudioChannel channel = e.getMember().getVoiceState().getChannel();
                audioManager.openAudioConnection(channel);

                eraseCommand(systemData, e.getMessage(), e.getGuild().getId());
            }
        }
    }

    @Override
    public String getExplanation(String serverId) {
        return systemData.getCommandPrefix(serverId) + "summon to put the bot into the voice channel.";
    }
}
