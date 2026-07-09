package com.mits.service.score;

import java.util.List;
import com.mits.dto.score.BadmintonScoreRequestDTO;
import com.mits.entity.score.BadmintonScore;

public interface BadmintonScoreService {

    BadmintonScore createScore(BadmintonScoreRequestDTO dto);

    BadmintonScore getScoreById(Long id);

    List<BadmintonScore> getAllScores();

    BadmintonScore updateScore(Long id, BadmintonScoreRequestDTO dto);

    void deleteScore(Long id);
}