package com.mits.service.score;

import java.util.List;

import com.mits.entity.score.VolleyballScore;

public interface VolleyballScoreService {

    VolleyballScore createScore(VolleyballScore score);

    VolleyballScore getScoreById(Long id);

    List<VolleyballScore> getAllScores();

    VolleyballScore updateScore(Long id, VolleyballScore score);

    void deleteScore(Long id);
}