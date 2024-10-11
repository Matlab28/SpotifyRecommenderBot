package com.example.spotifyrecommenderbot.controller;

import com.example.spotifyrecommenderbot.dto.spotify.TrackDto;
import com.example.spotifyrecommenderbot.service.SpotifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/api/top-tracks")
    public List<Map<String, Object>> getTopTracks() {
        return spotifyService.getTopTracks();
    }
}
