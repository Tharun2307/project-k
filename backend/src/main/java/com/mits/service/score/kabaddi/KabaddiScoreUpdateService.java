package com.mits.service.score.kabaddi;

import com.mits.entity.MatchEvent;
import com.mits.entity.score.KabaddiScore;

public interface KabaddiScoreUpdateService {
    void processEvent(KabaddiScore score, MatchEvent event, boolean isTeam1);
    void reverseEvent(KabaddiScore score, MatchEvent event, boolean isTeam1); // Added for Undo
}