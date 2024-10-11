package com.example.spotifyrecommenderbot.service;

import com.example.spotifyrecommenderbot.dto.gemini.Root;
import com.example.spotifyrecommenderbot.dto.request.SpotifyRequestDto;
import com.example.spotifyrecommenderbot.dto.telegram.request.TelegramSendDto;
import org.springframework.stereotype.Service;

@Service
public interface GeminiService {
    Root processSongRequest(TelegramSendDto dto);

    Root getLatestResponse();
}
