package com.mits.service.score;

import java.util.List;
import com.mits.dto.score.VolleyballScoreRequestDTO;
import com.mits.entity.score.VolleyballScore;

public interface VolleyballScoreService {

    VolleyballScore createScore(VolleyballScoreRequestDTO dto);

    VolleyballScore getScoreById(Long id);

    List<VolleyballScore> getAllScores();

    VolleyballScore updateScore(Long id, VolleyballScoreRequestDTO dto);

    void deleteScore(Long id);
}