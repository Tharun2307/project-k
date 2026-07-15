package com.mits.service.score.badminton;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.BadmintonScore;

public interface BadmintonScoringService {
    void processBadmintonEvent(MatchEvent event, BadmintonScore score);
    void recalculateScore(Match match);
}