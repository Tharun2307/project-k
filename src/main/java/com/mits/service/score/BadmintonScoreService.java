package com.mits.service.score;

import java.util.List;

import com.mits.entity.score.BadmintonScore;

public interface BadmintonScoreService {

    BadmintonScore createScore(BadmintonScore score);

    BadmintonScore getScoreById(Long id);

    List<BadmintonScore> getAllScores();

    BadmintonScore updateScore(Long id, BadmintonScore score);

    void deleteScore(Long id);

}