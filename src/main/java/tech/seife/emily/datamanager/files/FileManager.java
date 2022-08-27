package tech.seife.emily.datamanager.files;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private final File resourcesFile;
    private final File muted, mutedHistory, settings, songsQueue;
    private final Logger logger;
    private final Gson gson;

    public FileManager() {
        logger = LoggerFactory.getLogger(FileManager.class);
        gson = new Gson();

        resourcesFile = new File("src/main/resources");


        settings = new File(resourcesFile.getAbsolutePath() + File.separator + "settings.json");
        muted = new File(resourcesFile.getAbsolutePath() + File.separator + "muted.json");
        mutedHistory = new File(resourcesFile.getAbsolutePath() + File.separator + "mutedHistory.json");
        songsQueue = new File(resourcesFile.getAbsolutePath() + File.separator + "songsQueue.json");

        if (!resourcesFile.exists()) {
            resourcesFile.mkdirs();
            try {
                settings.createNewFile();
                muted.createNewFile();
                mutedHistory.createNewFile();
                songsQueue.createNewFile();
            } catch (IOException e) {
                logger.error("Failed to create the file, error message: " + e.getMessage());
            }
        }
    }

    public HashMap getGsonSongsQueue() {
        try {
            return gson.fromJson(new FileReader(songsQueue), HashMap.class);
        } catch (FileNotFoundException e) {
            logger.error(settings.getName() + " wasn't found");
        }
        return null;
    }

    public void saveGsonSongsQueue(Map map) {
        String json = gson.toJson(map);
        mutedHistory.delete();
        try {
            Files.write(songsQueue.toPath(), json.getBytes());
        } catch (IOException e) {
            logger.error("Failed to save json!\nErrorMessage: " + e.getMessage());
        }
    }


    public HashMap getGsonMutedHistory() {
        try {
            return gson.fromJson(new FileReader(mutedHistory), HashMap.class);
        } catch (FileNotFoundException e) {
            logger.error(settings.getName() + " wasn't found");
        }
        return null;
    }

    public void saveMutedHistoryFile(Map map) {
        String json = gson.toJson(map);
        mutedHistory.delete();
        try {
            Files.write(mutedHistory.toPath(), json.getBytes());
        } catch (IOException e) {
            logger.error("Failed to save json!\nErrorMessage: " + e.getMessage());
        }
    }


    public HashMap getGsonMuted() {
        try {
            return gson.fromJson(new FileReader(muted), HashMap.class);
        } catch (FileNotFoundException e) {
            logger.error(settings.getName() + " wasn't found");
        }
        return null;
    }

    public void saveMutesFile(Map map) {
        String json = gson.toJson(map);
        muted.delete();
        try {
            Files.write(muted.toPath(), json.getBytes());
        } catch (IOException e) {
            logger.error("Failed to save json!\nErrorMessage: " + e.getMessage());
        }
    }

    public void saveSettingsJson(Map map) {
        String json = gson.toJson(map);
        settings.delete();
        try {
            Files.write(settings.toPath(), json.getBytes());
        } catch (IOException e) {
            logger.error("Failed to save json!\nErrorMessage: " + e.getMessage());
        }
    }


    public HashMap getGsonSettings() {
        try {
            return gson.fromJson(new FileReader(settings), HashMap.class);
        } catch (FileNotFoundException e) {
            logger.error(settings.getName() + " wasn't found");
        }
        return null;
    }

    public Gson getGson() {
        return gson;
    }
}
