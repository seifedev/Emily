package tech.seife.emily.audiologic;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import tech.seife.emily.datamanager.data.system.SystemData;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class RetrieveVideo {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final SystemData systemData;

    public RetrieveVideo(SystemData systemData) {
        this.systemData = systemData;
    }


    public YouTube getService() {
        NetHttpTransport httpTransport = null;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            System.out.println("Inside getService()! error message: " + e.getMessage());
        }
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName("Discord Bot")
                .build();
    }

    /**
     *
     * @param query What to search for
     * @param serverId The ID of the server.
     * @return a VideoListResponse that has the details for what the member queries on YouTube.
     * @throws IOException
     */
    public VideoListResponse getVideo(String query, String serverId) throws IOException {
        YouTube youtubeService = getService();

        YouTube.Search.List requestForId = null;

        requestForId = youtubeService.search()
                .list(Collections.singletonList("id"));

        SearchListResponse response = null;
        response = requestForId.setKey(systemData.getYoutubeToken(serverId))
                .setMaxResults(1L)
                .setQ(query)
                .execute();
        String id = response.getItems().get(0).getId().getVideoId();


        YouTube.Videos.List request = youtubeService.videos()
                .list(Collections.singletonList("snippet,contentDetails"));

        VideoListResponse videoResponse = request.setKey(systemData.getYoutubeToken(serverId))
                .setId(Collections.singletonList(id))
                .execute();

        return videoResponse;
    }

}
