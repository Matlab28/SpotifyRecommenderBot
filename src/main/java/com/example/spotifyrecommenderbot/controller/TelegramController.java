//package com.example.spotifyrecommenderbot.controller;
//
//import com.example.spotifyrecommenderbot.dto.telegram.request.RootRequestDto;
//import com.example.spotifyrecommenderbot.dto.telegram.request.TelegramSendDto;
//import com.example.spotifyrecommenderbot.dto.telegram.response.RootResponseDto;
//import com.example.spotifyrecommenderbot.service.TelegramService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/spotify")
//public class TelegramController {
//    private final TelegramService service;
//
//    @GetMapping("/get-updates")
//    public RootRequestDto getUpdates() {
//        return service.getUpdateService();
//    }
//
//    @PostMapping("/send-message")
//    public RootResponseDto sendMessage(@RequestBody TelegramSendDto dto) {
//        return service.sendMessage(dto);
//    }
//}
