package com.example.spotifyrecommenderbot.dto.telegram.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class RootRequestDto {
    private Boolean ok;
    private ArrayList<ResultRequestDto> result;
}
