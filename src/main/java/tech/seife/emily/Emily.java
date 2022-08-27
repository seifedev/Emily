package tech.seife.emily;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.Nullable;
import tech.seife.emily.audio.AudioEvent;
import tech.seife.emily.commands.audio.*;
import tech.seife.emily.commands.entertainment.BotTalk;
import tech.seife.emily.commands.management.*;
import tech.seife.emily.commands.system.*;
import tech.seife.emily.datamanager.data.audio.QueueManager;
import tech.seife.emily.datamanager.data.mute.MutedUserManager;
import tech.seife.emily.datamanager.data.system.MuteManager;
import tech.seife.emily.datamanager.data.system.SystemManager;
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
    private AudioEvent trackScheduler;
    private AudioPlayer audioPlayer;
    private QueueManager queueDataManager;

    //Data
    private FileManager fileManager;
    private QueueManager queueManager;
    private MuteManager muteManager;
    private SystemManager systemData;

    //Events and commands
    private Commands commands;

    public Emily() {
        initialize();
        jda = loadJda();

        scheduler(jda);
        mutedUserManager.loadMutedUsers(fileManager);
        AudioSourceManagers.registerRemoteSources(audioManager);

        audioPlayer.addListener(new AudioEvent(audioManager, queueManager));

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

    private void registerEvents() {
        jda.addEventListener(new AutomaticUnmute(mutedUserManager));
        jda.addEventListener(new QueueReactions(jda, queueManager));
        jda.addEventListener(new PrivateMessages(systemData, jda));
    }

    @Nullable
    private JDA loadJda() {
        JDA jda = null;
        try {
            jda = JDABuilder.createDefault(systemData.getToken()).enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.DIRECT_MESSAGES).build().awaitReady();
        } catch (LoginException e) {
            System.out.println("Failed to login: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception: " + e.getMessage());
        }
        return jda;
    }

    private void scheduler(JDA jda) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleWithFixedDelay(new BotStatus(jda), 1, 10, TimeUnit.SECONDS);
    }

    private void initialize() {
        fileManager = new FileManager();
        queueManager = new QueueManager(fileManager);
        muteManager = new MuteManager(fileManager);
        systemData = new SystemManager(fileManager);

        mutedUserManager = new MutedUserManager(muteManager);
        queueDataManager = new QueueManager(fileManager);
        audioManager = new DefaultAudioPlayerManager();
        trackScheduler = new AudioEvent(audioManager, queueManager);
        commands = new Commands();

        audioPlayer = audioManager.createPlayer();
    }

    private void populateCommands() {
        commands.addCommand(new HelpCommand(systemData, commands));
        commands.addCommand(new SkipPartsOfMusic(systemData, audioPlayer));
        commands.addCommand(new SkipSong(systemData, audioPlayer));
        commands.addCommand(new PlayMusic(systemData, audioManager, audioPlayer, queueManager));
        commands.addCommand(new Summon(systemData, audioPlayer));
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
        commands.addCommand(new StopMusic(systemData, audioPlayer, queueManager));
        commands.addCommand(new ChangeVolume(systemData, audioPlayer));
        commands.addCommand(new SelfDestructSwitch(systemData));
        commands.addCommand(new SelfDestructDelay(systemData));
        commands.addCommand(new SetDmChannel(systemData));
    }

}
