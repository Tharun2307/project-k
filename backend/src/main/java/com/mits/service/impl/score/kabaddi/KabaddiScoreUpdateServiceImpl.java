package com.mits.service.impl.score.kabaddi;

import com.mits.entity.MatchEvent;
import com.mits.entity.score.KabaddiScore;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.service.score.kabaddi.KabaddiScoreUpdateService;

import org.springframework.stereotype.Service;

@Service
public class KabaddiScoreUpdateServiceImpl implements KabaddiScoreUpdateService {

    private final KabaddiScoreRepository kabaddiScoreRepository;

    public KabaddiScoreUpdateServiceImpl(KabaddiScoreRepository kabaddiScoreRepository) {
        this.kabaddiScoreRepository = kabaddiScoreRepository;
    }

    @Override
    public void processEvent(KabaddiScore score, MatchEvent event, boolean isTeam1) {
        String eventType = event.getEventType().toUpperCase();
        int pointsToAdd = 0;
        boolean resetEmptyRaids = false;

        if (eventType.contains("RAID_SUCCESS") || eventType.contains("RAID")) {
            int tackleCount = event.getTackledDefendersCount() != null ? event.getTackledDefendersCount() : 0;
            pointsToAdd += tackleCount;
            
            if (Boolean.TRUE.equals(event.getIsBonusCrossed())) {
                pointsToAdd += 1;
                if (isTeam1) score.setTeam1BonusPoints(score.getTeam1BonusPoints() + 1);
                else score.setTeam2BonusPoints(score.getTeam2BonusPoints() + 1);
            }
            resetEmptyRaids = true;
        } 
        else if (eventType.contains("TACKLE")) {
            pointsToAdd = 1; 
            if (event.getDefendersOnCourt() != null && event.getDefendersOnCourt() <= 3) {
                pointsToAdd = 2; // Super Tackle
                if (isTeam1) score.setTeam1SuperTackles(score.getTeam1SuperTackles() + 1);
                else score.setTeam2SuperTackles(score.getTeam2SuperTackles() + 1);
            }
            if (isTeam1) score.setTeam1TacklePoints(score.getTeam1TacklePoints() + pointsToAdd);
            else score.setTeam2TacklePoints(score.getTeam2TacklePoints() + pointsToAdd);
        }
        else if (eventType.contains("ALL_OUT")) {
            pointsToAdd += 2; // Lona bonus
            if (isTeam1) score.setTeam1AllOuts(score.getTeam1AllOuts() + 1);
            else score.setTeam2AllOuts(score.getTeam2AllOuts() + 1);
            resetEmptyRaids = true;
        }
        else if (eventType.contains("EMPTY_RAID") || Boolean.TRUE.equals(event.getIsDoOrDie())) {
            int currentEmptyRaids = isTeam1 ? score.getTeam1ConsecutiveEmptyRaids() : score.getTeam2ConsecutiveEmptyRaids();
            currentEmptyRaids++;
            if (currentEmptyRaids >= 3) {
                pointsToAdd += 1; // Do-or-Die point
                currentEmptyRaids = 0;
            }
            if (isTeam1) score.setTeam1ConsecutiveEmptyRaids(currentEmptyRaids);
            else score.setTeam2ConsecutiveEmptyRaids(currentEmptyRaids);
        }

        // Apply Points
        if (isTeam1) {
            score.setTeam1Points(score.getTeam1Points() + pointsToAdd);
            if (resetEmptyRaids) score.setTeam1ConsecutiveEmptyRaids(0);
        } else {
            score.setTeam2Points(score.getTeam2Points() + pointsToAdd);
            if (resetEmptyRaids) score.setTeam2ConsecutiveEmptyRaids(0);
        }

        kabaddiScoreRepository.save(score);
    }

    @Override
    public void reverseEvent(KabaddiScore score, MatchEvent event, boolean isTeam1) {
        // Simplified reversal: subtracts points based on event type. 
        // In a production app, you might want more granular reversal logic.
        String eventType = event.getEventType().toUpperCase();
        int pointsToSubtract = 0;

        if (eventType.contains("RAID_SUCCESS") || eventType.contains("RAID")) {
            pointsToSubtract += (event.getTackledDefendersCount() != null ? event.getTackledDefendersCount() : 0);
            if (Boolean.TRUE.equals(event.getIsBonusCrossed())) pointsToSubtract += 1;
        } else if (eventType.contains("TACKLE")) {
            pointsToSubtract = (event.getDefendersOnCourt() != null && event.getDefendersOnCourt() <= 3) ? 2 : 1;
        } else if (eventType.contains("ALL_OUT")) {
            pointsToSubtract += 2;
        }

        if (isTeam1) {
            score.setTeam1Points(Math.max(0, score.getTeam1Points() - pointsToSubtract));
        } else {
            score.setTeam2Points(Math.max(0, score.getTeam2Points() - pointsToSubtract));
        }
        kabaddiScoreRepository.save(score);
    }
}