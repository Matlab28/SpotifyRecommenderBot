package com.example.spotifyrecommenderbot.client;

import com.example.spotifyrecommenderbot.dto.telegram.request.RootRequestDto;
import com.example.spotifyrecommenderbot.dto.telegram.request.TelegramSendDto;
import com.example.spotifyrecommenderbot.dto.telegram.response.RootResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "telegramApi", url = "https://api.telegram.org/bot7479184744:AAHz3XN6T3nX6R1MFtc3PXWEXRiBCmhnJNc")
public interface TelegramClient {
    @GetMapping("/getUpdates?offset={value}")
    RootRequestDto getUpdates(@PathVariable Long value);

    @PostMapping("/sendMessage")
    RootResponseDto sendMessage(@RequestBody TelegramSendDto dto);
}
