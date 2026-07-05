package com.mits.repository.score;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mits.entity.Match;
import com.mits.entity.score.CricketScore;

@Repository
public interface CricketScoreRepository extends JpaRepository<CricketScore, Long> {

    Optional<CricketScore> findByMatch(Match match);

}