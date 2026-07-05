package com.mits.service.score;

import java.util.List;

import com.mits.entity.score.KabaddiScore;

public interface KabaddiScoreService {

    KabaddiScore createScore(KabaddiScore score);

    KabaddiScore getScoreById(Long id);

    List<KabaddiScore> getAllScores();

    KabaddiScore updateScore(Long id, KabaddiScore score);

    void deleteScore(Long id);
}