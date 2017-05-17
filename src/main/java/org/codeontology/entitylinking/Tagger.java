package org.codeontology.entitylinking;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tagger {
    private static final String TAGME_ENDPOINT = "https://tagme.d4science.org/tagme/tag";
    private final String GCUBE_TOKEN = Main.getGcubeToken();
    private static final int MAX_TRIES = 10;

    public Tagger() {

    }

    public List<TagMeAnnotation> tag(String text) {
        try {
            return tag(text, 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<TagMeAnnotation> tag(String text, int requestCount) throws TagMeException {
        if (requestCount > MAX_TRIES) {
            throw new TagMeException("Could not connect to TagMe endpoint. Skipping...");
        }
        try {
            HttpResponse<JsonNode> response = tagMeRequest(text);
            return extractAnnotations(response.getBody());
        } catch (UnirestException e) {
            System.out.println("Could not connect to TagMe endpoint. Retrying...");
            return tag(text, requestCount + 1);
        }
    }

    private HttpResponse<JsonNode> tagMeRequest(String text) throws UnirestException, TagMeException {
        HttpResponse<JsonNode> response = Unirest.post(TAGME_ENDPOINT)
                .field("text", text)
                .field("gcube-token", GCUBE_TOKEN)
                .asJson();

        if (response.getStatus() != 200) {
            throw new TagMeException(response.getStatus(), response.getStatusText());
        }
        return response;
    }

    private List<TagMeAnnotation> extractAnnotations(JsonNode body) {
        JSONObject jsonObject = body.getObject();
        JSONArray annotations = jsonObject.getJSONArray("annotations");
        int length = annotations.length();

        List<TagMeAnnotation> result = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            TagMeAnnotation annotation = buildAnnotation((JSONObject) annotations.get(i));
            result.add(annotation);
        }

        return result;
    }

    private TagMeAnnotation buildAnnotation(JSONObject annotation) {
        String title = annotation.getString("title");
        Double rho = annotation.getDouble("rho");
        return new TagMeAnnotation(title, rho);
    }
}
