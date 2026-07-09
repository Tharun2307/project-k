package com.mits.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mits.entity.Match;
import com.mits.enums.MatchStatus; // ✅ Ensure this is imported

public interface MatchRepository extends JpaRepository<Match, Long> {
    
    Optional<Match> findBySportAndTeam1AndTeam2(com.mits.entity.Sport sport, com.mits.entity.Team team1, com.mits.entity.Team team2);
    
    // ✅ ADD THIS LINE:
    long countByStatus(MatchStatus status);
}