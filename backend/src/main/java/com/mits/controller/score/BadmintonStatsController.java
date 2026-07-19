package com.mits.controller.score;

import com.mits.dto.score.badminton.BadmintonScorecardDTO;
import com.mits.entity.Match;
import com.mits.entity.score.BadmintonScore;
import com.mits.repository.MatchRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
public class BadmintonStatsController {

    private final MatchRepository matchRepository;
    private final BadmintonScoreRepository badmintonScoreRepository;

    public BadmintonStatsController(MatchRepository matchRepository, 
                                    BadmintonScoreRepository badmintonScoreRepository) {
        this.matchRepository = matchRepository;
        this.badmintonScoreRepository = badmintonScoreRepository;
    }

    @GetMapping("/{matchId}/badminton-scorecard")
    public ResponseEntity<BadmintonScorecardDTO> getBadmintonScorecard(@PathVariable Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with ID: " + matchId));

        if (!match.getSport().getSportName().equalsIgnoreCase("BADMINTON")) {
            throw new RuntimeException("This match is not a Badminton match!");
        }

        BadmintonScore score = badmintonScoreRepository.findByMatch(match)
                .orElse(new BadmintonScore());

        BadmintonScorecardDTO scorecard = new BadmintonScorecardDTO(
                match.getId(),
                match.getTeam1().getTeamName(),
                match.getTeam2().getTeamName(),
                score.getPlayer1SetsWon(),
                score.getPlayer2SetsWon(),
                score.getCurrentSet(),
                score.getPlayer1Points(),
                score.getPlayer2Points(),
                score.isMatchOver(),
                match.getStatus().name()
        );

        return ResponseEntity.ok(scorecard);
    }
}
