package com.example.spotifyrecommenderbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class SpotifyService {

    @Value("${spotify.access.token}")
    private String token;

    private final RestTemplate restTemplate;

    public SpotifyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getTopTracks() {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.spotify.com/v1/me/top/tracks")
                .queryParam("time_range", "long_term")
                .queryParam("limit", 5)
                .toUriString();

        // Set headers with the authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the request and get the response
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        // Return the list of items from the response
        return (List<Map<String, Object>>) response.getBody().get("items");
    }
}
