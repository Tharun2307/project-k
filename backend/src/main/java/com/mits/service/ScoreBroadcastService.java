package com.mits.service;

public interface ScoreBroadcastService {
   
    void broadcastScoreUpdate(Long matchId, Object scoreData);
}