package tech.seife.emily.datamanager.data.moderate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import tech.seife.emily.datamanager.files.FileManager;

import java.lang.annotation.Documented;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModerationFileHandler implements ModerationData {

    private final FileManager fileManager;
    private final Gson gson;

    public ModerationFileHandler(FileManager fileManager) {
        this.fileManager = fileManager;
        gson = fileManager.getGson();
    }

    @Override
    public Set<MutedUsersData> getMutedData() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonMuted()), JsonObject.class);

        Set<MutedUsersData> mutedUsersData = new HashSet<>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            for (Map.Entry<String, JsonElement> details : entry.getValue().getAsJsonObject().entrySet()) {
                if (details.getValue().isJsonObject()) {
                    JsonObject member = details.getValue().getAsJsonObject();

                    mutedUsersData.add(new MutedUsersData(details.getKey(), entry.getKey(), LocalDateTime.parse(member.get("mutedDate").getAsString()), LocalDateTime.parse(member.get("releaseDate").getAsString()), member.get("reason").getAsString()));
                }
            }
        }
        return mutedUsersData;
    }

    @Override
    public boolean saveMute(MutedUsersData mutedUsersData) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonMuted()), JsonObject.class);

        if (!jsonObject.has(mutedUsersData.serverId())) {
            jsonObject.add(mutedUsersData.serverId(), new JsonObject());
            jsonObject.get(mutedUsersData.serverId()).getAsJsonObject().addProperty("latestServerMuteId", 0);
        }

        JsonObject serverId = jsonObject.get(mutedUsersData.serverId()).getAsJsonObject();

        if (serverId.has(mutedUsersData.memberId())) {
            return false;
        }

        JsonObject mute = new JsonObject();

        mute.addProperty("muteId", serverId.get("latestServerMuteId").getAsLong() + 1);
        mute.addProperty("mutedDate", mutedUsersData.mutedDate().toString());
        mute.addProperty("releaseDate", mutedUsersData.releaseDate().toString());
        mute.addProperty("reason", mutedUsersData.reason());


        fileManager.saveMutesFile(gson.fromJson(gson.toJson(jsonObject), Map.class));

        return true;
    }

    @Override
    public boolean removeMute(String serverId, String memberId) {
        if (!isMemberMuted(serverId, memberId)) {
            return false;
        }

        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonMuted()), JsonObject.class);

        jsonObject.get(serverId).getAsJsonObject().remove(memberId);


        fileManager.saveMutesFile(gson.fromJson(gson.toJson(jsonObject), Map.class));
        return true;
    }

    @Override
    public boolean moveMuteToHistory(MutedUsersData mutedUsersData, String reason) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonMutedHistory()), JsonObject.class);


        if (!jsonObject.has(mutedUsersData.serverId())) {
            jsonObject.add(mutedUsersData.serverId(), new JsonObject());
        }

        JsonObject server = jsonObject.get(mutedUsersData.serverId()).getAsJsonObject();

        JsonObject muteDetails = new JsonObject();

        muteDetails.addProperty("mutedDated", mutedUsersData.mutedDate().toString());
        muteDetails.addProperty("releaseDate", mutedUsersData.releaseDate().toString());
        muteDetails.addProperty("muteReason", mutedUsersData.reason());
        muteDetails.addProperty("unMuteReason", reason);

        if (server.has(mutedUsersData.memberId())) {
            server.get(mutedUsersData.memberId()).getAsJsonObject().add(String.valueOf(server.get(mutedUsersData.memberId()).getAsJsonObject().size() + 1), muteDetails);
        } else {
            server.add(mutedUsersData.memberId(), muteDetails);
        }

        fileManager.saveMutedHistoryFile(gson.fromJson(gson.toJson(jsonObject), Map.class));
        return true;
    }

    @Override
    public boolean isMemberMuted(String serverId, String memberId) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(fileManager.getGsonMuted()), JsonObject.class);

        return jsonObject.has(serverId) && jsonObject.get(serverId).getAsJsonObject().has(memberId);
    }
}
