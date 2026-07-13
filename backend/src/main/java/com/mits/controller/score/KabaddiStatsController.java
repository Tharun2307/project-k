package com.mits.controller.score;

import com.mits.dto.score.kabaddi.KabaddiScorecardDTO;
import com.mits.entity.Match;
import com.mits.repository.MatchRepository;
import com.mits.service.impl.score.kabaddi.KabaddiScoringLogic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
public class KabaddiStatsController {

    private final MatchRepository matchRepository;
    private final KabaddiScoringLogic kabaddiScoringLogic;

    public KabaddiStatsController(MatchRepository matchRepository, 
                                  KabaddiScoringLogic kabaddiScoringLogic) {
        this.matchRepository = matchRepository;
        this.kabaddiScoringLogic = kabaddiScoringLogic;
    }

    /**
     * Public endpoint to fetch the detailed Kabaddi scorecard.
     * Example: GET /api/matches/5/kabaddi-scorecard
     */
    @GetMapping("/{matchId}/kabaddi-scorecard")
    public ResponseEntity<KabaddiScorecardDTO> getKabaddiScorecard(@PathVariable Long matchId) {
        
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with ID: " + matchId));

        if (!match.getSport().getSportName().equalsIgnoreCase("KABADDI")) {
            throw new RuntimeException("This match is not a Kabaddi match!");
        }

        KabaddiScorecardDTO scorecard = kabaddiScoringLogic.generateScorecard(match);
        return ResponseEntity.ok(scorecard);
    }
}