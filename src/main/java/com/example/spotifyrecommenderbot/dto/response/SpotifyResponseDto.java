package com.example.spotifyrecommenderbot.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SpotifyResponseDto {
    private String song;
    private String artist;
    private String albumCoverUrl;
    private List<String> previewUrls;
}
