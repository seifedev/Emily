package tech.seife.emily;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.Nullable;
import tech.seife.emily.audiologic.GuildAudioPlayer;
import tech.seife.emily.commands.audio.*;
import tech.seife.emily.commands.entertainment.BotTalk;
import tech.seife.emily.commands.management.*;
import tech.seife.emily.commands.system.*;
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
import tech.seife.emily.events.PrivateMessages;
import tech.seife.emily.events.QueueReactions;
import tech.seife.emily.scheduler.BotStatus;

import javax.security.auth.login.LoginException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Emily {

    //System
    private final JDA jda;
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

    //Events and commands
    private Commands commands;

    public Emily() {
        initialize();
        jda = loadJda();

        scheduler(jda);
        mutedUserManager.loadMutedUsers(moderationData);


        populateCommands();
        registerEvents();
        registerCommands();
    }

    public static void main(String[] args) {
        new Emily();
    }

    private void registerCommands() {
        commands.getCommands().forEach(jda::addEventListener);
    }


    /**
     * @return if it can load the JDA it returns the JDA otherwise null.
     */
    @Nullable
    private JDA loadJda() {
        JDA jda = null;
        try {
            jda = JDABuilder.createDefault(systemData.getDiscordBotToken()).enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.DIRECT_MESSAGES).build().awaitReady();
        } catch (LoginException e) {
            // TODO: 10/4/2022 to replace it with a proper logger
            System.out.println("Failed to login: " + e.getMessage());
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

        commands = new Commands();
    }

    /**
     * Adding all the commands to a list, so they can be easily added later.
     * it's a bit more readable and less clustered
     */
    private void populateCommands() {
        commands.addCommand(new HelpCommand(systemData, commands));

        commands.addCommand(new SetMusicChannel(systemData));
        commands.addCommand(new Summon(systemData, guildAudioPlayer));
        commands.addCommand(new PlayMusic(systemData, audioManager, queueManager, audioData, guildAudioPlayer));
        commands.addCommand(new SkipPartsOfMusic(systemData, guildAudioPlayer));
        commands.addCommand(new SkipSong(systemData, guildAudioPlayer));
        commands.addCommand(new StopMusic(systemData, queueManager, guildAudioPlayer));
        commands.addCommand(new ChangeVolume(systemData, guildAudioPlayer));
        commands.addCommand(new Yeet(systemData));


        commands.addCommand(new CleanMessages(systemData));
        commands.addCommand(new Contact(jda, systemData));
        commands.addCommand(new SetPrefix(jda, systemData));
        commands.addCommand(new SlowMode(systemData));
        commands.addCommand(new SetOwner(systemData));
        commands.addCommand(new Mute(mutedUserManager, systemData));
        commands.addCommand(new UnMute(jda, mutedUserManager, systemData));
        commands.addCommand(new ShowQueue(systemData, queueManager, jda));
        commands.addCommand(new BotTalk(systemData));
        commands.addCommand(new SelfDestructSwitch(systemData));
        commands.addCommand(new SelfDestructDelay(systemData));
        commands.addCommand(new SetDmChannel(systemData));
    }

    /**
     * It registers all the events for the bot.
     */
    private void registerEvents() {
        jda.addEventListener(new AutomaticUnmute(mutedUserManager));
        jda.addEventListener(new QueueReactions(jda, queueManager));
        jda.addEventListener(new PrivateMessages(systemData, jda));

    }

}
