package com.mits.controller.score;

import com.mits.dto.score.badminton.BadmintonScorecardDTO;
import com.mits.entity.Match;
import com.mits.entity.score.BadmintonScore;
import com.mits.enums.BadmintonMatchState;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
public class BadmintonStatsController {

    private final MatchService matchService;
    private final BadmintonScoreRepository badmintonScoreRepository;

    public BadmintonStatsController(MatchService matchService, BadmintonScoreRepository badmintonScoreRepository) {
        this.matchService = matchService;
        this.badmintonScoreRepository = badmintonScoreRepository;
    }

    @GetMapping("/{matchId}/badminton-scorecard")
    public ResponseEntity<BadmintonScorecardDTO> getBadmintonScorecard(@PathVariable Long matchId) {
        Match match = matchService.getMatchById(matchId);
        Optional<BadmintonScore> optScore = badmintonScoreRepository.findByMatch(match);
        
        if (optScore.isEmpty()) return ResponseEntity.notFound().build();

        BadmintonScore score = optScore.get();
        BadmintonScorecardDTO dto = new BadmintonScorecardDTO();

        dto.setMatchId(match.getId());
        dto.setPlayer1Name(match.getTeam1().getTeamName()); 
        dto.setPlayer2Name(match.getTeam2().getTeamName());
        
        dto.setPlayer1SetsWon(score.getPlayer1SetsWon());
        dto.setPlayer2SetsWon(score.getPlayer2SetsWon());
        dto.setCurrentSet(score.getCurrentSet());
        dto.setPlayer1Points(score.getPlayer1Points());
        dto.setPlayer2Points(score.getPlayer2Points());
        
        dto.setMatchState(score.getMatchState());
        dto.setCurrentServerId(score.getCurrentServerId());
        dto.setInterval((score.getPlayer1Points() == 11 && !score.isPlayer2IntervalReached()) || 
                        (score.getPlayer2Points() == 11 && !score.isPlayer1IntervalReached()));
        dto.setRallyHistory(score.getRallyHistory());
        dto.setResult(score.getResult());

        if (score.getMatchState() != BadmintonMatchState.COMPLETED) {
            int leaderPoints = Math.max(score.getPlayer1Points(), score.getPlayer2Points());
            int pointsNeeded = 21 - leaderPoints;
            dto.setMatchSituation("First to " + (pointsNeeded > 0 ? 21 : 30) + " points (win by 2)");
        }

        return ResponseEntity.ok(dto);
    }
}
