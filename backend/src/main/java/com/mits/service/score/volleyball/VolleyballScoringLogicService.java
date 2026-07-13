package com.mits.service.score.volleyball;

import com.mits.entity.MatchEvent;
import com.mits.entity.score.VolleyballScore;

public interface VolleyballScoringLogicService {
    void processVolleyballEvent(MatchEvent event, VolleyballScore score);
}