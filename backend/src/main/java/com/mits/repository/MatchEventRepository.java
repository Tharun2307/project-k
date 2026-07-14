package com.mits.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
    
    // Fetch all events for a specific match (to calculate the score later)
    List<MatchEvent> findByMatchIdOrderByTimestampDesc(Long matchId);
    List<MatchEvent> findByMatchOrderByTimestampAsc(Match match);
}