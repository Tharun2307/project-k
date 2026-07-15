package com.mits.service.score;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;

public interface ScoreUpdateService {

    void updateScore(MatchEvent event);
    void reverseScore(MatchEvent event);
	void recalculateScore(Match match);
}