package com.example.spotifyrecommenderbot.controller;

import com.example.spotifyrecommenderbot.dto.gemini.Root;
import com.example.spotifyrecommenderbot.dto.request.SpotifyRequestDto;
import com.example.spotifyrecommenderbot.dto.telegram.request.RootRequestDto;
import com.example.spotifyrecommenderbot.dto.telegram.request.TelegramSendDto;
import com.example.spotifyrecommenderbot.dto.telegram.response.RootResponseDto;
import com.example.spotifyrecommenderbot.service.RecommenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-recommender")
public class RecommenderController {
    private final RecommenderService service;

    @GetMapping("/get-updates")
    public RootRequestDto getUpdates() {
        return service.getUpdateService();
    }

    @PostMapping("/send-message")
    public RootResponseDto sendMessage(@RequestBody TelegramSendDto dto) {
        return service.sendMessage(dto);
    }

    @PostMapping("/getRecommendation")
    public ResponseEntity<?> getSongs(@RequestBody TelegramSendDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.processSongRequest(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Something went wrong while processing song request...");
        }
    }

    @GetMapping("/latestResponse")
    public ResponseEntity<Root> getLatestResponse() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getLatestResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
