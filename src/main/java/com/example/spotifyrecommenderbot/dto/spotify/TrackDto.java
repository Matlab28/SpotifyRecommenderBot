package com.example.spotifyrecommenderbot.dto.spotify;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TrackDto {
    private String name;
    private List<ArtistDto> artists;
}
