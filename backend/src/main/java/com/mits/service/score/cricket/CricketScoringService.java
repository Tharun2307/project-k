package com.mits.service.score.cricket;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.CricketScore;

public interface CricketScoringService {
    void processCricketEvent(MatchEvent event, CricketScore score);
    void recalculateScore(Match match);
}