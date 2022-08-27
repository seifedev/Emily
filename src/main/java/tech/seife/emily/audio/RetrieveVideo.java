package tech.seife.emily.audio;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class RetrieveVideo {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


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

    public VideoListResponse getVideo(String query) throws IOException {
        YouTube youtubeService = getService();

        YouTube.Search.List requestForId = null;

        requestForId = youtubeService.search()
                .list(Collections.singletonList("id"));

        SearchListResponse response = null;
        response = requestForId.setKey("add your key")
                .setMaxResults(1L)
                .setQ(query)
                .execute();
        String id = response.getItems().get(0).getId().getVideoId();


        YouTube.Videos.List request = youtubeService.videos()
                .list(Collections.singletonList("snippet,contentDetails"));

        VideoListResponse videoResponse = request.setKey("AIzaSyBouW-UY0Y5EdHqvaltpp4S3nZ1PI52UXY")
                .setId(Collections.singletonList(id))
                .execute();

        return videoResponse;
    }

}
