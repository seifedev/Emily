package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.system.SystemData;

public class ShowQueue extends ListenerAdapter {

    private final SystemData systemData;
    private final QueueManager queueManager;
    private final JDA jda;
    private final Messenger messenger;

    public ShowQueue(SystemData systemData, QueueManager queueManager, JDA jda, Messenger messenger) {
        this.systemData = systemData;
        this.queueManager = queueManager;
        this.jda = jda;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("showQueue")) return;
        if (!canRunCommand(e)) return;
        sendMessage(e);
    }

    private boolean canRunCommand(@NotNull SlashCommandInteractionEvent e) {
        if (!systemData.hasMusicChannel(e.getGuild().getId())) {
             e.replyEmbeds(messenger.getMessageEmbed("createMusicChannel")).queue();
            return false;
        } else if (!systemData.getMusicChannel(e.getGuild().getId()).equals(e.getChannel().getId())) {
             e.replyEmbeds(messenger.getMessageEmbed("notAMusicChannel")).queue();
            return false;
        } else if (e.getMember() == null || e.getMember().getVoiceState() == null || !e.getMember().getVoiceState().inAudioChannel()) {
             e.replyEmbeds(messenger.getMessageEmbed("notInVoiceChat")).queue();
            return false;
        } else if (e.getOptions().isEmpty()) {
             e.replyEmbeds(messenger.getMessageEmbed("doesntTakeArguments")).queue();
            return false;
        }
        return true;
    }


    private void sendMessage(@NotNull SlashCommandInteractionEvent e) {
        EmbedBuilder embedBuilder = queueManager.getEmbedQueue(0, 0, jda, new EmbedBuilder(), e.getGuild().getId());

        e.getChannel().sendMessageEmbeds(embedBuilder.build()).queue(message -> {
            queueManager.addReactions(message);
            queueManager.addQueueManager(e.getMember().getUser().getId(), message.getId(), embedBuilder);
            queueManager.setUserPage(e.getUser().getAsMention(), 0);
        });
    }
}
