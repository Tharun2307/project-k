package com.mits.repository.score;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mits.entity.Match;
import com.mits.entity.score.BadmintonScore;

@Repository
public interface BadmintonScoreRepository
        extends JpaRepository<BadmintonScore, Long> {

    Optional<BadmintonScore> findByMatch(Match match);

}