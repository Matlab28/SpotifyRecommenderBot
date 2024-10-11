package com.example.spotifyrecommenderbot.dto.telegram.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TelegramSendDto {
    @JsonProperty("chat_id")
    private String chatId;
    private String text;
    private String geminiResponse;
}
