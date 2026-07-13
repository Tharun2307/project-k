package com.mits.service.score.kabaddi;

import com.mits.entity.MatchEvent;
import com.mits.entity.score.KabaddiScore;

public interface KabaddiScoreUpdateService {

    /**
     * Processes a MatchEvent and updates the KabaddiScore entity.
     * 
     * @param score   The existing KabaddiScore entity to update.
     * @param event   The MatchEvent that just occurred.
     * @param isTeam1 True if the scoring team is Team 1, false if Team 2.
     */
    void processEvent(KabaddiScore score, MatchEvent event, boolean isTeam1);
}