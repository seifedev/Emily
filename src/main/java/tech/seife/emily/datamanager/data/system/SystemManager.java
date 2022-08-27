package tech.seife.emily.datamanager.data.system;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tech.seife.emily.datamanager.files.FileManager;

import java.util.Map;

public class SystemManager implements SystemData {

    private final FileManager fileManager;
    private final Gson gson;
    private String commandPrefix;

    public SystemManager(FileManager fileManager) {
        this.fileManager = fileManager;
        gson = fileManager.getGson();
        commandPrefix = getCommandPrefix();
    }

    public String getMusicChannel() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        return jsonObject.has("musicChannel") ? jsonObject.get("musicChannel").getAsString() : "";
    }

    public void setMusicChannel(String id) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        jsonObject.remove("musicChannel");
        jsonObject.addProperty("musicChannel", id);

        fileManager.saveSettingsJson(gson.fromJson(jsonObject, Map.class));
    }

    public String getDmChannel() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        return jsonObject.has("dmChannel") ? jsonObject.get("dmChannel").getAsString() : "";

    }

    public void setDmChannel(String id) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        jsonObject.remove("dmChannel");
        jsonObject.addProperty("dmChannel", id);

        fileManager.saveSettingsJson(gson.fromJson(jsonObject, Map.class));
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public String getOwner() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);


        return jsonObject.has("owner") ? jsonObject.get("owner").getAsString() : "";
    }

    public void setOwner(String id) {
        JsonObject settings = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);


        settings.remove("owner");

        settings.addProperty("owner", id);

        fileManager.saveSettingsJson(gson.fromJson(settings, Map.class));
    }

    public String getToken() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        return jsonObject.has("token") ? jsonObject.get("token").getAsString() : "";
    }

    public void setNewPrefix(String newPrefix) {
        JsonObject settings = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        settings.remove("commandPrefix");
        settings.addProperty("commandPrefix", newPrefix);

        fileManager.saveSettingsJson(gson.fromJson(settings, Map.class));
        commandPrefix = newPrefix;
    }

    public void changeSelfDestruct() {
        JsonObject settings = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        boolean value = getSelfDestructValue();

        settings.addProperty("selfDestruct", !value);
        fileManager.saveSettingsJson(gson.fromJson(settings, Map.class));
    }

    public boolean getSelfDestructValue() {
        JsonObject settings = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (settings.get("selfDestruct") == null) {
            settings.addProperty("selfDestruct", false);
            return false;
        }

        return settings.get("selfDestruct").getAsBoolean();
    }

    public long getSelfDestructDelay() {
        JsonObject settings = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (settings.get("selfDestructDelay") == null) {
            return 5L;
        }

        return settings.get("selfDestructDelay").getAsLong();
    }

    public void setSelfDestructDelay(long amount) {
        JsonObject settings = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (settings.get("selfDestructDelay") != null) {
            settings.remove("selfDestructDelay");
        }
        settings.addProperty("selfDestructDelay", amount);

        fileManager.saveSettingsJson(gson.fromJson(settings, Map.class));
    }

}
