package tech.seife.emily.datamanager.data.system;

import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import tech.seife.emily.datamanager.data.system.SystemData;
import tech.seife.emily.datamanager.files.FileManager;

import java.util.Map;

public class SystemFileHandler implements SystemData {

    private final FileManager fileManager;
    private final Gson gson;

    public SystemFileHandler(FileManager fileManager) {
        this.fileManager = fileManager;
        gson = fileManager.getGson();
    }


    @Override
    public boolean hasMusicChannel(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        return jsonObject.has(serverId) && jsonObject.get(serverId).getAsJsonObject().has("musicChannel");
    }

    @Nullable
    @Override
    public String getMusicChannel(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (jsonObject.has(serverId) && jsonObject.get(serverId).getAsJsonObject().has("musicChannel")) {
            return jsonObject.get(serverId).getAsJsonObject().get("musicChannel").getAsString();
        }

        return null;

    }

    @Override
    public boolean setMusicChannel(String serverId, String channelId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId)) {
            jsonObject.add(serverId, new JsonObject());
        }

        if (jsonObject.get(serverId).getAsJsonObject().has("musicChannel")) {
            jsonObject.get(serverId).getAsJsonObject().remove("musicChannel");
        }

        jsonObject.get(serverId).getAsJsonObject().addProperty("musicChannel", channelId);

        fileManager.saveSettingsJson(gson.fromJson(gson.toJson(jsonObject), Map.class));

        return true;
    }

    @Nullable
    @Override
    public String getDmChannel(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (hasDmChannel(serverId)) {
            return jsonObject.get(serverId).getAsJsonObject().get("dmChannel").getAsString();
        }

        return null;
    }

    @Override
    public boolean hasDmChannel(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        return jsonObject.has(serverId) && jsonObject.get(serverId).getAsJsonObject().has("dmChannel");
    }

    @Override
    public boolean setDmChannel(String serverId, String channelId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId)) {
            jsonObject.add(serverId, new JsonObject());
        } else if (jsonObject.get(serverId).getAsJsonObject().has("dmChannel")) {
            jsonObject.get(serverId).getAsJsonObject().remove("dmChannel");
        }

        jsonObject.get(serverId).getAsJsonObject().addProperty("dmChannel", channelId);
        fileManager.saveSettingsJson(gson.fromJson(gson.toJson(jsonObject), Map.class));
        return true;
    }

    @Override
    public String getCommandPrefix(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId)) {
            jsonObject.add(serverId, new JsonObject());
        }

        if (!jsonObject.get(serverId).getAsJsonObject().has("commandPrefix")) {
            jsonObject.get(serverId).getAsJsonObject().addProperty("commandPrefix", "!");
        }

        return jsonObject.get(serverId).getAsJsonObject().get("commandPrefix").getAsString();
    }

    @Nullable
    @Override
    public String getOwner(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId)) {
            return null;
        } else if (!jsonObject.get(serverId).getAsJsonObject().has("owner")) {
            return null;
        }

        return jsonObject.get(serverId).getAsJsonObject().get("owner").getAsString();
    }

    @Override
    public boolean setOwner(String serverId, String memberId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId)) {
            jsonObject.add(serverId, new JsonObject());
        }

        if (jsonObject.get(serverId).getAsJsonObject().has("owner")) {
            jsonObject.get(serverId).getAsJsonObject().remove("owner");
        }

        jsonObject.get(serverId).getAsJsonObject().addProperty("owner", memberId);
        fileManager.saveSettingsJson(gson.fromJson(gson.toJson(jsonObject), Map.class));

        return true;
    }

    @Nullable
    @Override
    public String getDiscordBotToken() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (jsonObject.has("global") && jsonObject.get("global").getAsJsonObject().has("discordBotToken")) {
            return jsonObject.get("global").getAsJsonObject().get("discordBotToken").getAsString();
        }

        return null;
    }

    @Override
    public String getYoutubeToken(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId) || !jsonObject.get(serverId).getAsJsonObject().has("youtubeToken")) {
            return null;
        }

        return jsonObject.get(serverId).getAsJsonObject().get("youtubeToken").getAsString();
    }

    @Override
    public boolean setNewPrefix(String serverId, String newPrefix) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId)) {
            jsonObject.add(serverId, new JsonObject());
        } else if (jsonObject.get(serverId).getAsJsonObject().has("commandPrefix")) {
            jsonObject.get(serverId).getAsJsonObject().remove("commandPrefix");
        }

        jsonObject.get(serverId).getAsJsonObject().addProperty("commandPrefix", newPrefix);
        fileManager.saveSettingsJson(gson.fromJson(gson.toJson(jsonObject), Map.class));

        return true;
    }

    @Override
    public boolean changeSelfDestruct(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId)) {
            jsonObject.add(serverId, new JsonObject());
        }

        boolean value = false;
        if (jsonObject.get(serverId).getAsJsonObject().has("selfDestruct")) {
            value = jsonObject.get(serverId).getAsJsonObject().get("selfDestruct").getAsBoolean();
            jsonObject.get(serverId).getAsJsonObject().remove("selfDestruct");
        }

        jsonObject.get(serverId).getAsJsonObject().addProperty("selfDestruct", !value);

        fileManager.saveSettingsJson(gson.fromJson(gson.toJson(jsonObject), Map.class));

        return true;

    }

    @Override
    public boolean getSelfDestructValue(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId) || !jsonObject.get(serverId).getAsJsonObject().has("selfDestruct")) {
            return false;
        }

        return jsonObject.get(serverId).getAsJsonObject().get("selfDestruct").getAsBoolean();
    }

    @Override
    public long getSelfDestructDelay(String serverId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId) || !jsonObject.get(serverId).getAsJsonObject().has("selfDestructDelay")) {
            return -1L;
        }

        return jsonObject.get(serverId).getAsJsonObject().get("selfDestructDelay").getAsLong();
    }

    @Override
    public boolean setSelfDestructDelay(String serverId, long amount) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonSettings()), JsonObject.class);

        if (!jsonObject.has(serverId)) {
            jsonObject.add(serverId, new JsonObject());
        }
        if (jsonObject.get(serverId).getAsJsonObject().has("selfDestructDelay")) {
            jsonObject.get(serverId).getAsJsonObject().remove("selfDestructDelay");
        }

        jsonObject.get(serverId).getAsJsonObject().addProperty("selfDestructDelay", amount);

        return true;
    }
}
