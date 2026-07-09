package com.mits.service.score;

import java.util.List;
import com.mits.dto.score.KabaddiScoreRequestDTO;
import com.mits.entity.score.KabaddiScore;

public interface KabaddiScoreService {

    KabaddiScore createScore(KabaddiScoreRequestDTO dto);

    KabaddiScore getScoreById(Long id);

    List<KabaddiScore> getAllScores();

    KabaddiScore updateScore(Long id, KabaddiScoreRequestDTO dto);

    void deleteScore(Long id);
}