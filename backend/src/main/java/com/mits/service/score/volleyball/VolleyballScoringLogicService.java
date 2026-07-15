package com.mits.service.score.volleyball;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.VolleyballScore;

public interface VolleyballScoringLogicService {
    void processVolleyballEvent(MatchEvent event, VolleyballScore score);
    void recalculateScore(Match match); // Added for Undo support
}