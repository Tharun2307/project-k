package com.mits.controller.score;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mits.dto.score.volleyball.VolleyballScorecardDTO;
import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.VolleyballScore;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.MatchRepository;
import com.mits.repository.score.VolleyballScoreRepository;

@RestController
@RequestMapping("/api/live-score/volleyball")
public class VolleyballStatsController {

    private final VolleyballScoreRepository volleyballScoreRepository;
    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;

    // Constructor Injection (No Lombok)
    public VolleyballStatsController(VolleyballScoreRepository volleyballScoreRepository,
                                     MatchRepository matchRepository,
                                     MatchEventRepository matchEventRepository) {
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.matchRepository = matchRepository;
        this.matchEventRepository = matchEventRepository;
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<VolleyballScorecardDTO> getVolleyballScorecard(@PathVariable Long matchId) {
        
        // 1. Fetch Match
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + matchId));

        // 2. Fetch Volleyball Score
        VolleyballScore score = volleyballScoreRepository.findByMatchId(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Volleyball score not found for match id: " + matchId));

        // 3. Fetch Recent Events (Last 5 events for the "Recent Events" ticker)
        List<MatchEvent> recentEvents = matchEventRepository.findByMatchIdOrderByTimestampDesc(matchId)
                .stream()
                .limit(5)
                .collect(Collectors.toList());

        // 4. Map to DTO
        VolleyballScorecardDTO dto = new VolleyballScorecardDTO();
        dto.setMatchId(match.getId());
        dto.setTeam1Name(match.getTeam1().getTeamName());
        dto.setTeam2Name(match.getTeam2().getTeamName());
        
        dto.setTeam1SetsWon(score.getTeam1SetsWon());
        dto.setTeam2SetsWon(score.getTeam2SetsWon());
        dto.setCurrentSet(score.getCurrentSet());
        dto.setTeam1Points(score.getTeam1Points());
        dto.setTeam2Points(score.getTeam2Points());
        
        dto.setTeam1TimeoutsRemaining(score.getTeam1TimeoutsRemaining());
        dto.setTeam2TimeoutsRemaining(score.getTeam2TimeoutsRemaining());
        dto.setServingTeam(score.getServingTeam());
        dto.setMatchOver(score.isMatchOver());
        dto.setMatchStatus(match.getStatus().name()); // e.g., "LIVE" or "COMPLETED"

        // Format recent events for frontend display
        List<String> recentEventStrings = recentEvents.stream()
                .map(event -> {
                    String teamName = event.getTeam() != null ? event.getTeam().getTeamName() : "Unknown";
                    return teamName + ": " + event.getEventType();
                })
                .collect(Collectors.toList());
        dto.setRecentEvents(recentEventStrings);

        return ResponseEntity.ok(dto);
    }
}