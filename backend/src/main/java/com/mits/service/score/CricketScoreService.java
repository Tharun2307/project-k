package com.mits.service.score;

import java.util.List;
import com.mits.dto.score.CricketScoreRequestDTO;
import com.mits.entity.score.CricketScore;

public interface CricketScoreService {

    CricketScore createScore(CricketScoreRequestDTO dto);

    CricketScore getScoreById(Long id);

    List<CricketScore> getAllScores();

    CricketScore updateScore(Long id, CricketScoreRequestDTO dto);

    void deleteScore(Long id);
}