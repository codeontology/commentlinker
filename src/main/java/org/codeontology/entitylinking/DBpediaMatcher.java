package org.codeontology.entitylinking;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DBpediaMatcher {
    private static final String DBPEDIA_PAGE = "http://dbpedia.org/page/";
    private static final String DBPEDIA_RESOURCE = "http://dbpedia.org/resource/";

    public List<String> matchAll(List<String> titles) {
        return titles.stream()
            .map(StringEscapeUtils::unescapeHtml4)
            .map(s -> s.replaceAll(" ", "_"))
            .filter(this::isDBpediaResource)
            .map(s -> s.replaceAll(",|!|\"|\\?|:", ""))
            .map(s -> DBPEDIA_RESOURCE + s)
            .collect(Collectors.toList());
    }

    private boolean isDBpediaResource(String resource) {
        try {
            HttpResponse<String> response = Unirest.post(DBPEDIA_PAGE + resource).asString();
            return response.getStatus() == 200;
        } catch (UnirestException e) {
            return false;
        }
    }
}
