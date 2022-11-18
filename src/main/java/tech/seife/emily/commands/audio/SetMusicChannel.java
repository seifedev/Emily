package tech.seife.emily.commands.audio;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.seife.emily.Messenger;
import tech.seife.emily.datamanager.data.system.SystemData;

public class SetMusicChannel extends ListenerAdapter {

    private final SystemData systemData;
    private final Messenger messenger;

    public SetMusicChannel(SystemData SystemData, Messenger messenger) {
        this.systemData = SystemData;
        this.messenger = messenger;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (!e.getName().equalsIgnoreCase("setMusicChannel")) return;
        systemData.setMusicChannel(e.getGuild().getId(), e.getChannel().getId());

        e.replyEmbeds(messenger.getMessageEmbed("successfullyCreatedMusicChannel")).queue();
    }
}
