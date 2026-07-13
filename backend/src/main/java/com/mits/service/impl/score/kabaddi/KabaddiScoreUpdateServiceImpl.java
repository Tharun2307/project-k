package com.mits.service.impl.score.kabaddi;

import com.mits.entity.MatchEvent;
import com.mits.entity.score.KabaddiScore;
import com.mits.service.score.kabaddi.KabaddiScoreUpdateService;
import org.springframework.stereotype.Service;

@Service
public class KabaddiScoreUpdateServiceImpl implements KabaddiScoreUpdateService {

    @Override
    public void processEvent(KabaddiScore score, MatchEvent event, boolean isTeam1) {
        // Get the event type as a String
        String eventType = event.getEventType();

        if (isTeam1) {
            updateTeam1(score, eventType);
        } else {
            updateTeam2(score, eventType);
        }
    }

    private void updateTeam1(KabaddiScore score, String eventType) {
        switch (eventType) {
            case "RAID_POINT":
                score.setTeam1Raids(score.getTeam1Raids() + 1);
                score.setTeam1Points(score.getTeam1Points() + 1);
                break;

            case "BONUS_POINT":
                score.setTeam1BonusPoints(score.getTeam1BonusPoints() + 1);
                score.setTeam1Points(score.getTeam1Points() + 1);
                break;

            case "SUPER_TACKLE":
                // A super tackle gives 1 tackle point + 1 super tackle bonus (Total 2 points)
                score.setTeam1TacklePoints(score.getTeam1TacklePoints() + 1);
                score.setTeam1SuperTackles(score.getTeam1SuperTackles() + 1);
                score.setTeam1Points(score.getTeam1Points() + 2);
                break;

            case "TACKLE_POINT":
                score.setTeam1TacklePoints(score.getTeam1TacklePoints() + 1);
                score.setTeam1Points(score.getTeam1Points() + 1);
                break;

            case "ALL_OUT":
                // Track that Team 1 got all-out
                score.setTeam1AllOuts(score.getTeam1AllOuts() + 1);
                break;

            default:
                // Ignore events not specific to Kabaddi scoring (e.g., timeouts)
                break;
        }
    }

    private void updateTeam2(KabaddiScore score, String eventType) {
        switch (eventType) {
            case "RAID_POINT":
                score.setTeam2Raids(score.getTeam2Raids() + 1);
                score.setTeam2Points(score.getTeam2Points() + 1);
                break;

            case "BONUS_POINT":
                score.setTeam2BonusPoints(score.getTeam2BonusPoints() + 1);
                score.setTeam2Points(score.getTeam2Points() + 1);
                break;

            case "SUPER_TACKLE":
                score.setTeam2TacklePoints(score.getTeam2TacklePoints() + 1);
                score.setTeam2SuperTackles(score.getTeam2SuperTackles() + 1);
                score.setTeam2Points(score.getTeam2Points() + 2);
                break;

            case "TACKLE_POINT":
                score.setTeam2TacklePoints(score.getTeam2TacklePoints() + 1);
                score.setTeam2Points(score.getTeam2Points() + 1);
                break;

            case "ALL_OUT":
                // Track that Team 2 got all-out
                score.setTeam2AllOuts(score.getTeam2AllOuts() + 1);
                break;

            default:
                break;
        }
    }
}