package com.mits.service;

import com.mits.dto.LiveScoreResponse;

public interface ScoreBroadcastService {
    
    void broadcastScoreUpdate(Long matchId, LiveScoreResponse liveScore);
    
}