package com.mits.service.impl.score.kabaddi;

import com.mits.dto.score.kabaddi.KabaddiScorecardDTO;
import com.mits.entity.Match;
import com.mits.entity.score.KabaddiScore;
import com.mits.repository.score.KabaddiScoreRepository;
import org.springframework.stereotype.Service;

@Service
public class KabaddiScoringLogic {

    private final KabaddiScoreRepository kabaddiScoreRepository;

    public KabaddiScoringLogic(KabaddiScoreRepository kabaddiScoreRepository) {
        this.kabaddiScoreRepository = kabaddiScoreRepository;
    }

    public KabaddiScorecardDTO generateScorecard(Match match) {
        // 1. Fetch the score (or return a blank scorecard if none exists yet)
        KabaddiScore score = kabaddiScoreRepository.findByMatch(match)
                .orElseGet(() -> new KabaddiScore()); 

        // 2. Calculate Derived Stats (Complex Logic)
        // In Kabaddi, a team starts with 7 defenders. 
        // We estimate defenders remaining by subtracting All-Outs from 7.
        int team1DefendersRemaining = Math.max(0, 7 - score.getTeam1AllOuts());
        int team2DefendersRemaining = Math.max(0, 7 - score.getTeam2AllOuts());

        // Calculate who is leading
        String currentLead;
        if (score.getTeam1Points() > score.getTeam2Points()) {
            currentLead = match.getTeam1().getTeamName() + " leads by " + (score.getTeam1Points() - score.getTeam2Points());
        } else if (score.getTeam2Points() > score.getTeam1Points()) {
            currentLead = match.getTeam2().getTeamName() + " leads by " + (score.getTeam2Points() - score.getTeam1Points());
        } else {
            currentLead = "Match is Tied";
        }

        // 3. Map to DTO
        return new KabaddiScorecardDTO(
                match.getId(),
                match.getTeam1().getTeamName(),
                match.getTeam2().getTeamName(),
                score.getTeam1Points(), score.getTeam1Raids(), score.getTeam1TacklePoints(),
                score.getTeam1BonusPoints(), score.getTeam1SuperTackles(), score.getTeam1AllOuts(),
                score.getTeam2Points(), score.getTeam2Raids(), score.getTeam2TacklePoints(),
                score.getTeam2BonusPoints(), score.getTeam2SuperTackles(), score.getTeam2AllOuts(),
                team1DefendersRemaining, team2DefendersRemaining, currentLead
        );
    }
}