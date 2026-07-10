package com.mits.service.score;

import com.mits.dto.LiveScoreResponse;

public interface LiveScoreService {
    LiveScoreResponse getLiveScoreByMatchId(Long matchId);
}