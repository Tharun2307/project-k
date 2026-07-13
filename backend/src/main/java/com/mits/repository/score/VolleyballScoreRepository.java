package com.mits.repository.score;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mits.entity.Match;
import com.mits.entity.score.VolleyballScore;

@Repository
public interface VolleyballScoreRepository extends JpaRepository<VolleyballScore, Long> {

    Optional<VolleyballScore> findByMatch(Match match);

    // Added for fast lookup directly by Match ID in the Stats Controller
    Optional<VolleyballScore> findByMatchId(Long matchId);
}