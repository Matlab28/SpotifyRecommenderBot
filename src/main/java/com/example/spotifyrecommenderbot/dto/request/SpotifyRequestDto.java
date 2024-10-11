package com.example.spotifyrecommenderbot.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SpotifyRequestDto {
    private String userInput;
    private String geminiResponse;
}
