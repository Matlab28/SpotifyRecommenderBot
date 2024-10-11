package com.example.spotifyrecommenderbot.dto.spotify;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ArtistDto {
    private String id; // Unique identifier for the artist
    private String name; // Name of the artist
    private String genre; // Genre of the artist
    private List<String> albums; // List of albums by the artist
    private String imageUrl; // URL of the artist's image
    private int popularity; // Popularity score (if available)
}
