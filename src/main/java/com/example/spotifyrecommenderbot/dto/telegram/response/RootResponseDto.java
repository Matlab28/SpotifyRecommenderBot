package com.example.spotifyrecommenderbot.dto.telegram.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RootResponseDto {
    private Boolean ok;
    private ResultResponseDto result;
}
