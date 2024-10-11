package com.example.spotifyrecommenderbot.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyRepository extends JpaRepository<SpotifyEntity, Long> {
}
