package tech.seife.emily;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Nullable;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.commands.audio.*;
import tech.seife.emily.commands.management.*;
import tech.seife.emily.datamanager.data.audio.AudioData;
import tech.seife.emily.datamanager.data.audio.AudioFileHandler;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.moderate.ModerationData;
import tech.seife.emily.datamanager.data.moderate.ModerationFileHandler;
import tech.seife.emily.datamanager.data.moderate.MuteManager;
import tech.seife.emily.datamanager.data.moderate.MutedUserManager;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.data.system.SystemFileHandler;
import tech.seife.emily.datamanager.files.FileManager;
import tech.seife.emily.events.AutomaticUnmute;
import tech.seife.emily.events.QueueReactions;
import tech.seife.emily.scheduler.BotStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Emily {

    //System
    private final JDA jda;
    //Miscellaneous
    private final Messenger messenger;
    private MutedUserManager mutedUserManager;
    //Audio
    private AudioPlayerManager audioManager;
    private GuildAudioPlayer guildAudioPlayer;
    private AudioData audioData;
    //Data
    private SystemData systemData;
    private ModerationData moderationData;
    private FileManager fileManager;
    private QueueManager queueManager;
    private MuteManager muteManager;

    public Emily() {
        initialize();
        jda = loadJda();
        messenger = new Messenger(jda, fileManager);

        scheduler(jda);
        mutedUserManager.loadMutedUsers(moderationData);

        registerEvents();
        populateCommands();
    }

    public static void main(String[] args) {
        new Emily();
    }

    /**
     * @return if it can load the JDA it returns the JDA otherwise null.
     */
    @Nullable
    private JDA loadJda() {
        JDA jda = null;
        try {
            jda = JDABuilder.createLight(systemData.getDiscordBotToken()).build().awaitReady();
        } catch (InterruptedException e) {
            // TODO: 10/4/2022 to replace it with a proper logger
            System.out.println("Interrupted exception: " + e.getMessage());
        }
        return jda;
    }

    /**
     * Dedicating one threat for the status of the bot.
     *
     * @param jda A reference to the JDA object.
     */
    private void scheduler(JDA jda) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleWithFixedDelay(new BotStatus(jda), 1, 10, TimeUnit.SECONDS);
    }

    /**
     * Initializing various classes that are needed for the bot to function properly.
     * Everything that must be initialized within Emily class goes here.
     */
    private void initialize() {
        fileManager = new FileManager();

        audioData = new AudioFileHandler(fileManager);
        audioManager = new DefaultAudioPlayerManager();
        guildAudioPlayer = new GuildAudioPlayer(audioManager);

        queueManager = new QueueManager(audioData);
        systemData = new SystemFileHandler(fileManager);

        moderationData = new ModerationFileHandler(fileManager);
        muteManager = new MuteManager(moderationData);
        mutedUserManager = new MutedUserManager(muteManager);
    }

    /**
     * Adding all the commands to a list, so they can be easily added later.
     * it's a bit more readable and less clustered
     */
    private void populateCommands() {
        commandsWithArguments();
        commandsWithoutArguments();
    }

    private void commandsWithoutArguments() {
        List<CommandData> commandData = new ArrayList<>();

        Guild guild = jda.getGuildById("");

        commandData.add(guild.upsertCommand("skipsong", "Stop the current song"));

        commandData.add(guild.upsertCommand("stopmusic", "Stop playing the current"));

        commandData.add(guild.upsertCommand("join", "force the bot to join in a voice channel."));

        commandData.add(guild.upsertCommand("yeet", "Kick all the members out of the voice channel."));

        commandData.add(guild.upsertCommand("setmusicchannel", "Change or create a new music channel!"));

        commandData.add(guild.upsertCommand("showqueue", "Prints the songs in the queue."));

        guild.updateCommands().addCommands(commandData).queue();
    }

    private void commandsWithArguments() {
        Guild guild = jda.getGuildById("/*nothing here*/");
        List<CommandData> commandData = new ArrayList<>();

        commandData.add(guild.upsertCommand("contact", "Send a private message while impersonating the bot, requires to tag the member.").addOption(OptionType.MENTIONABLE, "tag", "Tag a member to send them a message.").addOption(OptionType.STRING, "message", "What you want to tell them."));

        commandData.add(guild.upsertCommand("clean", "Clean a certain amount of messages from the chat.").addOption(OptionType.INTEGER, "amount", "how many messages to delete."));

        commandData.add(guild.upsertCommand("talk", "The bot will imitate what you said.").addOption(OptionType.STRING, "message", "What the bot should say."));

        commandData.add(guild.upsertCommand("changevolume", "Change the volume of the music that is being played.").addOption(OptionType.INTEGER, "percentage", "the percentage to increase/decrease the volume"));

        commandData.add(guild.upsertCommand("slowmode", "Enable/Disable the slow mode in a channel.").addOption(OptionType.INTEGER, "delay", "the amount of delay in seconds between each message per user"));

        commandData.add(guild.upsertCommand("seek", "Change your time stamp in the music").addOption(OptionType.INTEGER, "seconds", "go forwards/backwards by a X amount of seconds"));

        commandData.add(guild.upsertCommand("playmusic", "Start playing a song, provide URL or name").addOption(OptionType.STRING, "song", "Either the URL to the song or the name of the song."));

        List<OptionData> muteData = new ArrayList<>();

        muteData.add(new OptionData(OptionType.MENTIONABLE, "member", "The member to mute)"));
        muteData.add(new OptionData(OptionType.STRING, "duration", "for how long to mute"));
        muteData.add(new OptionData(OptionType.STRING, "reason", "The reason to mute him"));

        commandData.add(guild.upsertCommand("mute", "Mute a member.").addOptions(muteData));

        List<OptionData> unMuteData = new ArrayList<>();

        unMuteData.add(new OptionData(OptionType.MENTIONABLE, "member", "The member to unmute"));
        unMuteData.add(new OptionData(OptionType.STRING, "reason", "The reason to remove the mute"));

        commandData.add(guild.upsertCommand("unmute", "Remove the mute from a member.").addOptions(unMuteData));

        guild.updateCommands().addCommands(commandData).queue();
    }

    /**
     * It registers all the events for the bot.
     */
    private void registerEvents() {
        jda.addEventListener(new ChangeVolume(systemData, guildAudioPlayer, messenger));
        jda.addEventListener(new PlayMusic(systemData, audioManager, queueManager, audioData, guildAudioPlayer, messenger));
        jda.addEventListener(new SetMusicChannel(systemData, messenger));
        jda.addEventListener(new ShowQueue(systemData, queueManager, jda, messenger));
        jda.addEventListener(new SkipPartsOfMusic(systemData, guildAudioPlayer, messenger));
        jda.addEventListener(new SkipSong(systemData, guildAudioPlayer, messenger));
        jda.addEventListener(new JoinChannel(systemData, messenger));
        jda.addEventListener(new CleanMessages(systemData, messenger));
        jda.addEventListener(new Contact(jda, systemData, messenger));
        jda.addEventListener(new Mute(mutedUserManager, systemData, messenger));
        jda.addEventListener(new SlowMode(systemData, messenger));
        jda.addEventListener(new UnMute(jda, mutedUserManager, systemData, messenger));
        jda.addEventListener(new AutomaticUnmute(mutedUserManager));
        jda.addEventListener(new QueueReactions(jda, queueManager));
    }

}
