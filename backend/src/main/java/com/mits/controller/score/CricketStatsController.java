package com.mits.controller.score;

import com.mits.dto.score.cricket.CricketScorecardDTO;
import com.mits.entity.Match;
import com.mits.entity.score.cricket.BatsmanStats;
import com.mits.entity.score.cricket.BowlerStats;
import com.mits.entity.score.CricketScore;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        
        if (optScore.isEmpty()) return ResponseEntity.notFound().build();

        CricketScore score = optScore.get();
        CricketScorecardDTO dto = new CricketScorecardDTO();

        dto.setTeam1Name(match.getTeam1().getTeamName());
        dto.setTeam2Name(match.getTeam2().getTeamName());
        
        dto.setTeam1Runs(score.getTeam1Runs());
        dto.setTeam1Wickets(score.getTeam1Wickets());
        dto.setTeam1Overs(score.getTotalLegalBallsTeam1() / 6);
        dto.setTeam1Balls(score.getTotalLegalBallsTeam1() % 6);
        
        dto.setTeam2Runs(score.getTeam2Runs());
        dto.setTeam2Wickets(score.getTeam2Wickets());
        dto.setTeam2Overs(score.getTotalLegalBallsTeam2() / 6);
        dto.setTeam2Balls(score.getTotalLegalBallsTeam2() % 6);

        dto.setTeam1Score(formatScore(score.getTeam1Runs(), score.getTeam1Wickets(), score.getTotalLegalBallsTeam1()));
        dto.setTeam2Score(formatScore(score.getTeam2Runs(), score.getTeam2Wickets(), score.getTotalLegalBallsTeam2()));

        dto.setTarget(score.getTarget());
        dto.setCurrentRunRate(score.getCurrentRunRate());
        dto.setRequiredRunRate(score.getRequiredRunRate());
        dto.setResult(score.getResult());
        dto.setMatchState(score.getMatchState());
        dto.setCurrentInnings(score.getCurrentInnings());
        dto.setPartnershipRuns(score.getPartnershipRuns());
        dto.setPartnershipBalls(score.getPartnershipBalls());
        dto.setFallOfWickets(score.getFallOfWicketsList());
        dto.setLastOverBalls(score.getLastOverBalls());
        dto.setRecentBalls(score.getRecentBalls());

        if (score.getCurrentInnings() == 2 && score.getResult() == null) {
            int battingRuns = score.getBattingTeam() == 1 ? score.getTeam1Runs() : score.getTeam2Runs();
            int runsNeeded = Math.max(0, score.getTarget() - battingRuns);
            int maxBalls = score.getTotalOversInInnings() * 6;
            int legalBalls = score.getBattingTeam() == 1 ? score.getTotalLegalBallsTeam1() : score.getTotalLegalBallsTeam2();
            int ballsRemaining = Math.max(0, maxBalls - legalBalls);
            int wicketsRemaining = 10 - (score.getBattingTeam() == 1 ? score.getTeam1Wickets() : score.getTeam2Wickets());
            
            dto.setRunsNeeded(runsNeeded);
            dto.setBallsRemaining(ballsRemaining);
            dto.setWicketsRemaining(wicketsRemaining);
            dto.setMatchSituation("Need " + runsNeeded + " runs from " + ballsRemaining + " balls");
        }

        List<BatsmanStats> notOutBatsmen = score.getBatsmanStatsList().stream()
                .filter(b -> !b.isOut()).limit(2).collect(Collectors.toList());
        dto.setCurrentBatsmen(notOutBatsmen);

        if (score.getCurrentBowlerId() != null) {
            BowlerStats currentBowler = score.getBowlerStatsList().stream()
                    .filter(b -> b.getPlayerId().equals(score.getCurrentBowlerId()))
                    .findFirst().orElse(null);
            dto.setCurrentBowler(currentBowler);
        }

        return ResponseEntity.ok(dto);
    }

    private String formatScore(int runs, int wickets, int totalLegalBalls) {
        int overs = totalLegalBalls / 6;
        int balls = totalLegalBalls % 6;
        return runs + "/" + wickets + " (" + overs + "." + balls + ")";
    }
}