package com.example.spotifyrecommenderbot.service;

import com.example.spotifyrecommenderbot.client.GeminiApiClient;
import com.example.spotifyrecommenderbot.client.TelegramClient;
import com.example.spotifyrecommenderbot.dto.gemini.Candidate;
import com.example.spotifyrecommenderbot.dto.gemini.Root;
import com.example.spotifyrecommenderbot.dto.telegram.request.RootRequestDto;
import com.example.spotifyrecommenderbot.dto.telegram.request.TelegramSendDto;
import com.example.spotifyrecommenderbot.dto.telegram.response.RootResponseDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("recommenderService")
@Slf4j
@RequiredArgsConstructor
public class RecommenderService implements GeminiService {
    private final GeminiApiClient geminiApiClient;
    private final TelegramClient client;

    @Value("${gemini.api.key}")
    private String key;

    private Root latestUpdates;
    private Long lastUpdateId = 0L;

    public RootRequestDto getUpdateService() {
        RootRequestDto updates = client.getUpdates(0L);
        if (!updates.getResult().isEmpty()) {
            Integer updateId = updates.getResult().get(updates.getResult().size() - 1).getUpdateId();
            log.info("Message received from {}, ID {}", updates.getResult().get(0).getMessage().getFrom().getFirstName(),
                    updates.getResult().get(0).getMessage().getFrom().getId());
            return client.getUpdates(Long.valueOf(updateId));
        } else {
            log.error("No updates found for - {}, ID {}",
                    updates.getResult().get(0).getMessage().getFrom().getFirstName(),
                    updates.getResult().get(0).getMessage().getFrom().getId());
        }
        return updates;
    }

    public RootResponseDto sendMessage(TelegramSendDto dto) {
        try {
            return client.sendMessage(dto);
        } catch (Exception e) {
            log.error("Error sending message: ", e);
            return null;
        }
    }

    @Override
    public Root processSongRequest(TelegramSendDto dto) {
        String instruction = constructInstruction(dto);
        Root updates = getUpdates(instruction);
        String extractedText = extractedTextFromGeminiResponse(updates);
        dto.setText(extractedText); // Adjusting here to set the response text

        latestUpdates = updates;
        log.info("User's request has been processed");
        return latestUpdates;
    }

    @Override
    public Root getLatestResponse() {
        return latestUpdates;
    }

    private String constructInstruction(TelegramSendDto dto) {
        StringBuilder instruction = new StringBuilder();
        instruction.append("User's Input: ").append(dto.getText()).append("\n");
        instruction.append("Please provide recommendations for songs based on the user's input. " +
                "This could be just the title of a song, an artist's name, or both. " +
                "Please take both into account and recommend the best similar songs.");
        return instruction.toString();
    }

    private Root getUpdates(String instruction) {
        try {
            JsonObject json = new JsonObject();
            JsonArray partsArray = new JsonArray();
            JsonObject partsObject = new JsonObject();
            JsonArray contentsArray = new JsonArray();
            JsonObject contentsObject = new JsonObject();

            partsObject.add("text", new JsonPrimitive(instruction));
            partsArray.add(partsObject);
            contentsObject.add("parts", partsArray);
            json.add("contents", contentsObject);

            String content = json.toString();
            return geminiApiClient.getData(key, content);
        } catch (Exception e) {
            log.error("Error while getting response from Gemini AI: ", e);
            throw e;
        }
    }

    private String extractedTextFromGeminiResponse(Root updates) {
        StringBuilder textBuilder = new StringBuilder();

        if (updates != null && updates.getCandidates() != null) {
            for (Candidate candidate : updates.getCandidates()) {
                String text = candidate.getContent().getParts().get(0).getText();
                text = text.replace("*", "").trim();
                textBuilder.append(text).append("\n\n");
            }
        }

        String response = textBuilder.toString().trim();
        return response.replaceAll("(?i)\\bUser's Input:\\b", "\nUser's Input:\n");
    }

    public void recommendSong() {
        RootRequestDto updateService = getUpdateService();
        if (updateService != null && !updateService.getResult().isEmpty()) {
            String text = updateService.getResult().get(0).getMessage().getText();
            Long chatId = updateService.getResult().get(0).getMessage().getChat().getId();
            TelegramSendDto dto = new TelegramSendDto();
            dto.setChatId(String.valueOf(chatId));

            if (text.equals("/start")) {
                String msg = "Hi " + updateService.getResult().get(0).getMessage().getChat().getFirstName()
                        + ", welcome to the song recommender bot.\nPlease provide a song name or artist name, " +
                        "and I will recommend similar songs 🎵";
                dto.setText(msg);
            } else {
                if (text.trim().isEmpty()) {
                    String errorMsg = "Please enter a valid song name or artist.";
                    dto.setText(errorMsg);
                } else {
                    processSongRequest(dto); // Pass the song name to be processed
                }
            }
            sendMessage(dto);
        } else {
            log.error("No valid update service received for song recommendation.");
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void checkMessage() {
        RootRequestDto updateService = getUpdateService();
        if (updateService != null && !updateService.getResult().isEmpty()) {
            Integer latestUpdateId = updateService.getResult().get(updateService.getResult().size() - 1).getUpdateId();
            if (latestUpdateId > lastUpdateId) {
                lastUpdateId = Long.valueOf(latestUpdateId);
                recommendSong();
            }
        }
    }
}




//package com.example.spotifyrecommenderbot.service;
//
//import com.example.spotifyrecommenderbot.client.GeminiApiClient;
//import com.example.spotifyrecommenderbot.client.TelegramClient;
//import com.example.spotifyrecommenderbot.dto.gemini.Candidate;
//import com.example.spotifyrecommenderbot.dto.gemini.Root;
//import com.example.spotifyrecommenderbot.dto.telegram.request.RootRequestDto;
//import com.example.spotifyrecommenderbot.dto.telegram.request.TelegramSendDto;
//import com.example.spotifyrecommenderbot.dto.telegram.response.RootResponseDto;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonPrimitive;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Primary;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@Service("recommenderService")
//@Slf4j
//@RequiredArgsConstructor
//@Primary
//public class RecommenderService implements GeminiService {
//    private final GeminiApiClient geminiApiClient;
//    private final TelegramClient client;
//    @Value("${gemini.api.key}")
//    private String key;
//    private Root latestUpdates;
//    private Long lastUpdateId = 0L;
//
//
//    public RootRequestDto getUpdateService() {
//        RootRequestDto updates = client.getUpdates(0L);
//        if (!updates.getResult().isEmpty()) {
//            Integer updateId = updates.getResult().get(updates.getResult().size() - 1).getUpdateId();
//            log.info("Message got from {}, ID {}", updates.getResult().get(0).getMessage().getFrom().getFirstName(),
//                    updates.getResult().get(0).getMessage().getFrom().getId());
//            return client.getUpdates(Long.valueOf(updateId));
//        } else {
//            log.error("No updates found for - {}, ID {}",
//                    updates.getResult().get(0).getMessage().getFrom().getFirstName(),
//                    updates.getResult().get(0).getMessage().getFrom().getId());
//        }
//        return updates;
//    }
//
//    public RootResponseDto sendMessage(TelegramSendDto dto) {
//        try {
//            return client.sendMessage(dto);
//        } catch (Exception e) {
//            log.error("Error sending message: ", e);
//            return null;
//        }
//    }
//
//
//    @Override
//    public Root processSongRequest(TelegramSendDto dto) {
//
//        String instruction = constructInstruction(dto);
//        Root updates = getUpdates(instruction);
//        String extractedText = extractedTextFromGeminiResponse(updates);
//        dto.setGeminiResponse(extractedText);
//
//        latestUpdates = updates;
//        log.info("User's request has received");
//        return latestUpdates;
//    }
//
//    @Override
//    public Root getLatestResponse() {
//        return latestUpdates;
//    }
//
//    private String constructInstruction(TelegramSendDto dto) {
//        StringBuilder instruction = new StringBuilder();
//        instruction.append("User's Input: ").append(dto.getText()).append("\n");
//        instruction.append("Please provide, recommend songs based on user's input. It might be just" +
//                " the title of song, or the artist name. But even it might be the both of them together." +
//                " Please take into account both onf them.,band based on them recommend the best similar songs.");
//        return instruction.toString();
//    }
//
//    private Root getUpdates(String instruction) {
//        try {
//            JsonObject json = new JsonObject();
//            JsonArray partsArray = new JsonArray();
//            JsonObject partsObject = new JsonObject();
//            JsonArray contentsArray = new JsonArray();
//            JsonObject contentsObject = new JsonObject();
//
//            partsObject.add("text", new JsonPrimitive(instruction));
//            partsArray.add(partsObject);
//            contentsArray.add(partsArray);
//            contentsObject.add("parts", contentsArray);
//            json.add("contents", contentsObject);
//
//            String content = json.toString();
//            return geminiApiClient.getData(key, content);
//        } catch (Exception e) {
//            log.error("Error while getting response from Gemini AI: ", e);
//            throw e;
//        }
//    }
//
//    private String extractedTextFromGeminiResponse(Root updates) {
//        StringBuilder textBuilder = new StringBuilder();
//
//        if (updates.getCandidates() != null) {
//            for (Candidate candidate : updates.getCandidates()) {
//                String text = candidate.getContent().getParts().get(0).getText();
//                text = text.replace("*", "");
//                textBuilder.append(text).append("\n\n");
//            }
//        }
//
//        String response = textBuilder.toString().trim();
//
//        return response
//                .replaceAll("(?i)\\bUser's Input:\\b", "\nUser's Input:\n");
//    }
//
//
//    public RootResponseDto recommendSong() {
//        RootRequestDto updateService = getUpdateService();
//        if (updateService != null && !updateService.getResult().isEmpty()) {
//            String text = updateService.getResult().get(0).getMessage().getText();
//            Long chatId = updateService.getResult().get(0).getMessage().getChat().getId();
//            TelegramSendDto dto = new TelegramSendDto();
//            dto.setChatId(String.valueOf(chatId));
//
//            if (text.equals("/start")) {
//                String msg = "Hi " + updateService.getResult().get(0).getMessage().getChat().getFirstName()
//                        + ", welcome to song recommender.\nGive me the example song name or artist name," +
//                        " I will provide you the similar songs 🎵";
//                dto.setText(msg);
//            } else {
//                // Validate the input
//                if (text.trim().isEmpty()) {
//                    String errorMsg = "Please enter a valid song name or artist.";
//                    dto.setText(errorMsg);
//                    sendMessage(dto);
//                    return null; // Early return for invalid input
//                }
//                processSongRequest(dto); // Pass the song name to be processed
//            }
//            sendMessage(dto);
//        } else {
//            log.error("No valid update service received for song recommendation.");
//        }
//        return null;
//    }
//
////    public RootResponseDto recommendSong() {
////        RootRequestDto updateService = getUpdateService();
////        if (updateService != null) {
////            String text = updateService.getResult().get(0).getMessage().getText();
////            Long id = updateService.getResult().get(0).getMessage().getChat().getId();
////            TelegramSendDto dto = new TelegramSendDto();
////            dto.setChatId(String.valueOf(id));
////
////            if (text.equals("/start")) {
////                String msg = "Hi " + updateService.getResult().get(0).getMessage().getChat().getFirstName()
////                        + ", welcome to song recommender.\nGive me the example song name or artist name," +
////                        " I will provide you the similar songs 🎵";
////                dto.setText(msg);
////            } else {
////                processSongRequest(dto);
////            }
////            sendMessage(dto);
////        }
////        return null;
////    }
//
//    @Scheduled(fixedDelay = 1000)
//    public void checkMessage() {
//        RootRequestDto updateService = getUpdateService();
//        if (updateService != null && !updateService.getResult().isEmpty()) {
//            Integer latestUpdateId = updateService.getResult().get(updateService.getResult().size() - 1).getUpdateId();
//            if (latestUpdateId > lastUpdateId) {
//                lastUpdateId = Long.valueOf(latestUpdateId);
//                recommendSong();
//            }
//        }
//    }
//}
