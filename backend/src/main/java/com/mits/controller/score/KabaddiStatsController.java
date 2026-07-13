package com.mits.controller.score;

import com.mits.dto.score.kabaddi.KabaddiScorecardDTO;
import com.mits.entity.Match;
import com.mits.entity.score.KabaddiScore;
import com.mits.repository.MatchRepository;
import com.mits.repository.score.KabaddiScoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
public class KabaddiStatsController {

    private final MatchRepository matchRepository;
    private final KabaddiScoreRepository kabaddiScoreRepository;

    // Constructor Injection
    public KabaddiStatsController(MatchRepository matchRepository, 
                                  KabaddiScoreRepository kabaddiScoreRepository) {
        this.matchRepository = matchRepository;
        this.kabaddiScoreRepository = kabaddiScoreRepository;
    }

    /**
     * Public endpoint to fetch the detailed Kabaddi scorecard.
     * Example: GET /api/matches/5/kabaddi-scorecard
     */
    @GetMapping("/{matchId}/kabaddi-scorecard")
    public ResponseEntity<KabaddiScorecardDTO> getKabaddiScorecard(@PathVariable Long matchId) {
        
        // 1. Find the match
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with ID: " + matchId));

        // 2. Verify it is a Kabaddi match
        if (!match.getSport().getSportName().equalsIgnoreCase("KABADDI")) {
            throw new RuntimeException("This match is not a Kabaddi match!");
        }

        // 3. Fetch the score (or create a blank one with 0s if no events have happened yet)
        KabaddiScore score = kabaddiScoreRepository.findByMatch(match).orElse(new KabaddiScore());

        // 4. Calculate derived stats (Defenders remaining and Current Lead)
        int team1DefendersRemaining = Math.max(0, 7 - score.getTeam1AllOuts());
        int team2DefendersRemaining = Math.max(0, 7 - score.getTeam2AllOuts());

        String currentLead;
        if (score.getTeam1Points() > score.getTeam2Points()) {
            currentLead = match.getTeam1().getTeamName() + " leads by " + (score.getTeam1Points() - score.getTeam2Points());
        } else if (score.getTeam2Points() > score.getTeam1Points()) {
            currentLead = match.getTeam2().getTeamName() + " leads by " + (score.getTeam2Points() - score.getTeam1Points());
        } else {
            currentLead = "Match is Tied";
        }

        // 5. Map everything to the DTO
        KabaddiScorecardDTO scorecard = new KabaddiScorecardDTO(
                match.getId(),
                match.getTeam1().getTeamName(),
                match.getTeam2().getTeamName(),
                score.getTeam1Points(), score.getTeam1Raids(), score.getTeam1TacklePoints(),
                score.getTeam1BonusPoints(), score.getTeam1SuperTackles(), score.getTeam1AllOuts(),
                score.getTeam2Points(), score.getTeam2Raids(), score.getTeam2TacklePoints(),
                score.getTeam2BonusPoints(), score.getTeam2SuperTackles(), score.getTeam2AllOuts(),
                team1DefendersRemaining, team2DefendersRemaining, currentLead
        );

        // 6. Return the response
        return ResponseEntity.ok(scorecard);
    }
}