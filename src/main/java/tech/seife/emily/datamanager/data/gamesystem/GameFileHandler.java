package tech.seife.emily.datamanager.data.gamesystem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tech.seife.emily.datamanager.data.gamesystem.level.Level;
import tech.seife.emily.datamanager.files.FileManager;

import java.util.PriorityQueue;

public class GameFileHandler implements GameSystemData{

    private final FileManager fileManager;
    private final Gson gson;

    public GameFileHandler(FileManager fileManager) {
        this.fileManager = fileManager;
        gson = fileManager.getGson();
    }

    @Override
    public boolean hasLevels() {
        return false;
    }

    @Override
    public PriorityQueue<Level> loadLevels() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getLevels()), JsonObject.class);


        return null;
    }

    @Override
    public boolean addLevel(Level level) {
        return false;
    }

    @Override
    public boolean doesLevelExist(long id) {
        return false;
    }
}
