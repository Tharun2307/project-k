package com.mits.service.impl;

import com.mits.dto.LiveScoreResponse;
import com.mits.service.ScoreBroadcastService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ScoreBroadcastServiceImpl implements ScoreBroadcastService {

    private final SimpMessagingTemplate messagingTemplate;

    public ScoreBroadcastServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void broadcastScoreUpdate(Long matchId, LiveScoreResponse liveScore) {
        String destination = "/topic/match/" + matchId + "/score";
        messagingTemplate.convertAndSend(destination, liveScore);
        System.out.println("✅ Score broadcasted to: " + destination);
    }
}