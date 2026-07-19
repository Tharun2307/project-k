package com.mits.controller.score;

import com.mits.dto.score.volleyball.VolleyballScorecardDTO;
import com.mits.entity.Match;
import com.mits.entity.score.VolleyballScore;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/live-score/volleyball")
public class VolleyballStatsController {

    private final VolleyballScoreRepository volleyballScoreRepository;
    private final MatchService matchService;

    public VolleyballStatsController(VolleyballScoreRepository volleyballScoreRepository,
                                     MatchService matchService) {
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.matchService = matchService;
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<VolleyballScorecardDTO> getVolleyballScorecard(@PathVariable Long matchId) {
        Match match = matchService.getMatchById(matchId);
        Optional<VolleyballScore> optScore = volleyballScoreRepository.findByMatch(match);
        
        if (optScore.isEmpty()) {
            throw new ResourceNotFoundException("Volleyball score not found for match id: " + matchId);
        }

        VolleyballScore score = optScore.get();
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
        dto.setMatchStatus(match.getStatus().name());
        
        dto.setMatchState(score.getMatchState());
        dto.setSetHistory(score.getSetHistory());
        dto.setRallyHistory(score.getRallyHistory());
        dto.setSideSwitches(score.getSideSwitches());
        dto.setResult(score.getResult());

        return ResponseEntity.ok(dto);
    }
}