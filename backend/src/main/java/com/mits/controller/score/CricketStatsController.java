package com.mits.controller.score;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mits.dto.score.cricket.CricketScorecardDTO;
import com.mits.entity.Match;
import com.mits.entity.score.CricketScore;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.service.MatchService;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
public class CricketStatsController {

    private final MatchService matchService;
    private final CricketScoreRepository cricketScoreRepository;

    public CricketStatsController(MatchService matchService, CricketScoreRepository cricketScoreRepository) {
        this.matchService = matchService;
        this.cricketScoreRepository = cricketScoreRepository;
    }

    @GetMapping("/{matchId}/cricket-scorecard")
    public ResponseEntity<CricketScorecardDTO> getCricketScorecard(@PathVariable Long matchId) {
        Match match = matchService.getMatchById(matchId);
        Optional<CricketScore> optScore = cricketScoreRepository.findByMatch(match);
        
        if (optScore.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CricketScore score = optScore.get();
        CricketScorecardDTO dto = new CricketScorecardDTO();

        // Map raw data to DTO
        dto.setTeam1Name(match.getTeam1().getTeamName());
        dto.setTeam2Name(match.getTeam2().getTeamName());
        
        dto.setTeam1Runs(score.getTeam1Runs());
        dto.setTeam1Wickets(score.getTeam1Wickets());
        dto.setTeam1Overs(score.getTeam1Overs());
        dto.setTeam1Balls(score.getTeam1Balls());
        
        dto.setTeam2Runs(score.getTeam2Runs());
        dto.setTeam2Wickets(score.getTeam2Wickets());
        dto.setTeam2Overs(score.getTeam2Overs());
        dto.setTeam2Balls(score.getTeam2Balls());

        // Format the score strings (e.g., "145/4 (18.2)")
        dto.setTeam1Score(formatScore(score.getTeam1Runs(), score.getTeam1Wickets(), score.getTeam1Overs(), score.getTeam1Balls()));
        dto.setTeam2Score(formatScore(score.getTeam2Runs(), score.getTeam2Wickets(), score.getTeam2Overs(), score.getTeam2Balls()));

        dto.setTarget(score.getTarget());
        dto.setCurrentRunRate(score.getCurrentRunRate());
        dto.setRequiredRunRate(score.getRequiredRunRate());
        dto.setResult(score.getResult());

        // Calculate Match Situation dynamically
        if (score.getCurrentInnings() == 2 && score.getResult() == null) {
            int battingTeamRuns = (score.getBattingTeam() == 1) ? score.getTeam1Runs() : score.getTeam2Runs();
            int runsNeeded = score.getTarget() - battingTeamRuns;
            int ballsRemaining = (score.getTotalOversInInnings() * 6) - ((score.getBattingTeam() == 1 ? score.getTeam1Overs() : score.getTeam2Overs()) * 6 + (score.getBattingTeam() == 1 ? score.getTeam1Balls() : score.getTeam2Balls()));
            
            dto.setMatchSituation("Need " + runsNeeded + " runs from " + ballsRemaining + " balls");
        }

        return ResponseEntity.ok(dto);
    }

    private String formatScore(int runs, int wickets, int overs, int balls) {
        return runs + "/" + wickets + " (" + overs + "." + balls + ")";
    }
}