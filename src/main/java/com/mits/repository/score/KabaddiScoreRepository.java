package com.mits.repository.score;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mits.entity.Match;
import com.mits.entity.score.KabaddiScore;

@Repository
public interface KabaddiScoreRepository
        extends JpaRepository<KabaddiScore, Long> {

    Optional<KabaddiScore> findByMatch(Match match);

}