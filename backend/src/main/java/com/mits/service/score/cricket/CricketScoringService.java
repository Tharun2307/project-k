package com.mits.service.score.cricket;

import com.mits.entity.MatchEvent;
import com.mits.entity.score.CricketScore;

public interface CricketScoringService {
    
    /**
     * Processes a cricket match event, updates the CricketScore entity,
     * handles innings transitions, calculates run rates, and saves to the database.
     * 
     * @param event The MatchEvent recorded by the umpire
     * @param score The current CricketScore entity to be updated
     */
    void processCricketEvent(MatchEvent event, CricketScore score);
}