package com.mits.service.score;

import java.util.List;

import com.mits.entity.score.CricketScore;

public interface CricketScoreService {

    CricketScore createScore(CricketScore score);

    CricketScore getScoreById(Long id);

    List<CricketScore> getAllScores();

    CricketScore updateScore(Long id, CricketScore score);

    void deleteScore(Long id);
}