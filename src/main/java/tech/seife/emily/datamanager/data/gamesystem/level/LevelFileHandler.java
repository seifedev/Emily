package tech.seife.emily.datamanager.data.gamesystem.level;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tech.seife.emily.datamanager.files.FileManager;

import java.util.Map;

public class LevelFileHandler implements LevelData {

    private final FileManager fileManager;
    private final Gson gson;

    public LevelFileHandler(FileManager fileManager) {
        this.fileManager = fileManager;
        gson = fileManager.getGson();
    }

    @Override
    public boolean saveLevel(Level level) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getLevels()), JsonObject.class);

        if (jsonObject.has(String.valueOf(level.toString()))) {
            return false;
        }

        JsonObject jsonLevel = new JsonObject();

        jsonLevel.addProperty("maxExperience", level.getMaxExperience());
        jsonLevel.addProperty("experienceNeededToLevelUp", level.getExperienceToLevelUp());
        jsonLevel.addProperty("canExceedRequirements", level.getCanExceedRequirements());
        jsonLevel.addProperty("canExceedRequirements", level.getCanLoseLevel());

        jsonObject.add(String.valueOf(level.getId()), jsonLevel);


        fileManager.saveLevels(gson.fromJson(gson.toJson(jsonObject), Map.class));

        return true;
    }

    @Override
    public boolean deleteLevel(Level level) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getLevels()), JsonObject.class);

        if (jsonObject.has(String.valueOf(level.toString()))) {
            jsonObject.remove(String.valueOf(level.getId()));
            fileManager.saveLevels(gson.fromJson(gson.toJson(jsonObject), Map.class));
            return true;
        }

        return false;
    }
}
