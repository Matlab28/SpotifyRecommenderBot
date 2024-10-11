//package com.example.spotifyrecommenderbot.service;
//
//import com.example.spotifyrecommenderbot.client.GeminiApiClient;
//import com.example.spotifyrecommenderbot.client.TelegramClient;
//import com.example.spotifyrecommenderbot.dto.gemini.Root;
//import com.example.spotifyrecommenderbot.dto.request.SpotifyRequestDto;
//import com.example.spotifyrecommenderbot.dto.telegram.request.RootRequestDto;
//import com.example.spotifyrecommenderbot.dto.telegram.request.TelegramSendDto;
//import com.example.spotifyrecommenderbot.dto.telegram.response.RootResponseDto;
//import com.example.spotifyrecommenderbot.entity.SpotifyRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@Service("telegramService")
//@Slf4j
////@RequiredArgsConstructor
//public class TelegramService implements GeminiService {
//    private final TelegramClient client;
//    private Long lastUpdateId = 0L;
//
//    public TelegramService(ModelMapper modelMapper,
//                           SpotifyRepository repository,
//                           GeminiApiClient client,
//                           TelegramClient client1) {
//        super(modelMapper, repository, client);
//        this.client = client1;
//    }
//
//    public RootRequestDto getUpdateService() {
//        RootRequestDto updates = client.getUpdates(0L);
//        if (!updates.getResult().isEmpty()) {
//            Integer updateId = updates.getResult().get(updates.getResult().size() - 1).getUpdateId();
//            log.info("Message got from {}, ID {}", updates.getResult().get(0).getMessage().getFrom().getFirstName(),
//                    updates.getResult().get(0).getMessage().getFrom().getId());
//            return client.getUpdates(Long.valueOf(updateId));
//        }
//        return updates;
//    }
//
////    public RootResponseDto sendMessage(TelegramSendDto dto) {
////        return client.sendMessage(dto);
////    }
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
//    public RootResponseDto recommendSong() {
//        RootRequestDto updateService = getUpdateService();
//        if (updateService != null) {
//            String text = updateService.getResult().get(0).getMessage().getText();
//            Long id = updateService.getResult().get(0).getMessage().getChat().getId();
//            TelegramSendDto dto = new TelegramSendDto();
//            dto.setChatId(String.valueOf(id));
//
//            if (text.equals("/start")) {
//                String msg = "Hi " + updateService.getResult().get(0).getMessage().getChat().getFirstName()
//                        + ", welcome to song recommender.\nGive me the example song name or artist name," +
//                        " I will provide you the similar songs 🎵";
//                dto.setText(msg);
//            } else {
//                SpotifyRequestDto spotify = new SpotifyRequestDto();
//                spotify.setUserInput(text);
//                processSongRequest(spotify);
//            }
//            sendMessage(dto);
//        }
//        return null;
//    }
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
//
//    @Override
//    public Root processSongRequest(SpotifyRequestDto dto) {
//
//        return null;
//    }
//
//    @Override
//    public Root getLatestResponse() {
//        return null;
//    }
//}
