package com.mits.service.impl.score.volleyball;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.VolleyballScore;
import com.mits.enums.VolleyballMatchState;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.score.volleyball.VolleyballScoringLogicService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class VolleyballScoringLogicServiceImpl implements VolleyballScoringLogicService {

    private final VolleyballScoreRepository volleyballScoreRepository;
    private final MatchEventRepository matchEventRepository;

    public VolleyballScoringLogicServiceImpl(VolleyballScoreRepository volleyballScoreRepository, 
                                             MatchEventRepository matchEventRepository) {
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.matchEventRepository = matchEventRepository;
    }

    @Override
    public void processVolleyballEvent(MatchEvent event, VolleyballScore score) {
        processEventInternal(score, event);
        volleyballScoreRepository.save(score);
    }

    @Override
    public void recalculateScore(Match match) {
        VolleyballScore score = volleyballScoreRepository.findByMatch(match).orElse(new VolleyballScore());
        resetScore(score);
        
        List<MatchEvent> events = matchEventRepository.findByMatchOrderByTimestampAsc(match);
        for (MatchEvent event : events) {
            processEventInternal(score, event);
        }
        volleyballScoreRepository.save(score);
    }

    private void resetScore(VolleyballScore score) {
        score.setTeam1SetsWon(0);
        score.setTeam2SetsWon(0);
        score.setCurrentSet(1);
        score.setTeam1Points(0);
        score.setTeam2Points(0);
        score.setTeam1TimeoutsRemaining(2);
        score.setTeam2TimeoutsRemaining(2);
        score.setServingTeam(1);
        score.setMatchOver(false);
        score.setMatchState(VolleyballMatchState.FIRST_SET);
        score.setSetHistory(new ArrayList<>());
        score.setRallyHistory(new ArrayList<>());
        score.setSideSwitches(0);
        score.setResult(null);
    }

    private void processEventInternal(VolleyballScore score, MatchEvent event) {
        if (score.isMatchOver()) {
            return;
        }

        String eventType = event.getEventType().toUpperCase();
        boolean isTeam1 = event.getTeam() != null && 
                          event.getTeam().getId().equals(score.getMatch().getTeam1().getId());
        
        int scoringTeam = isTeam1 ? 1 : 2;
        String teamLabel = isTeam1 ? "T1" : "T2";

        // 1. Handle Timeouts
        if ("TIMEOUT".equals(eventType)) {
            if (isTeam1 && score.getTeam1TimeoutsRemaining() > 0) {
                score.setTeam1TimeoutsRemaining(score.getTeam1TimeoutsRemaining() - 1);
            } else if (!isTeam1 && score.getTeam2TimeoutsRemaining() > 0) {
                score.setTeam2TimeoutsRemaining(score.getTeam2TimeoutsRemaining() - 1);
            }
            return;
        }

        // 2. Handle Scoring Events
        if ("POINT".equals(eventType) || "ACE".equals(eventType) || 
            "SPIKE".equals(eventType) || "BLOCK".equals(eventType) || "ERROR".equals(eventType)) {
            
            if (isTeam1) {
                score.setTeam1Points(score.getTeam1Points() + 1);
            } else {
                score.setTeam2Points(score.getTeam2Points() + 1);
            }

            score.setServingTeam(scoringTeam);
            
            // Update rally history (keep last 10)
            score.getRallyHistory().add(teamLabel + ": " + eventType);
            if (score.getRallyHistory().size() > 10) {
                score.getRallyHistory().remove(0);
            }

            // 5th Set Side Switch at 8 points
            if (score.getCurrentSet() == 5 && score.getSideSwitches() == 0) {
                if (score.getTeam1Points() == 8 || score.getTeam2Points() == 8) {
                    score.setSideSwitches(1);
                }
            }

            checkAndResolveSetWin(score);
        }
    }

    private void checkAndResolveSetWin(VolleyballScore score) {
        int points1 = score.getTeam1Points();
        int points2 = score.getTeam2Points();
        int currentSet = score.getCurrentSet();
        
        boolean team1WinsSet = false;
        boolean team2WinsSet = false;

        if (currentSet < 5) {
            if (points1 >= 25 && (points1 - points2) >= 2) team1WinsSet = true;
            if (points2 >= 25 && (points2 - points1) >= 2) team2WinsSet = true;
        } else {
            if (points1 >= 15 && (points1 - points2) >= 2) team1WinsSet = true;
            if (points2 >= 15 && (points2 - points1) >= 2) team2WinsSet = true;
        }

        if (team1WinsSet) {
            score.setTeam1SetsWon(score.getTeam1SetsWon() + 1);
            score.getSetHistory().add(points1 + "-" + points2);
            resetForNextSet(score, 2); // Team 2 serves next set usually, or keep winner
        } else if (team2WinsSet) {
            score.setTeam2SetsWon(score.getTeam2SetsWon() + 1);
            score.getSetHistory().add(points1 + "-" + points2);
            resetForNextSet(score, 1);
        }

        if (score.getTeam1SetsWon() == 3 || score.getTeam2SetsWon() == 3) {
            score.setMatchOver(true);
            score.setMatchState(VolleyballMatchState.COMPLETED);
            score.setResult((score.getTeam1SetsWon() == 3 ? "Team 1" : "Team 2") + 
                            " won " + score.getTeam1SetsWon() + "-" + score.getTeam2SetsWon());
        } else {
            updateMatchState(score);
        }
    }

    private void resetForNextSet(VolleyballScore score, int nextServer) {
        score.setTeam1Points(0);
        score.setTeam2Points(0);
        score.setCurrentSet(score.getCurrentSet() + 1);
        score.setTeam1TimeoutsRemaining(2);
        score.setTeam2TimeoutsRemaining(2);
        score.setServingTeam(nextServer);
        score.setSideSwitches(0); // Reset side switches for the new set
    }

    private void updateMatchState(VolleyballScore score) {
        switch (score.getCurrentSet()) {
            case 1: score.setMatchState(VolleyballMatchState.FIRST_SET); break;
            case 2: score.setMatchState(VolleyballMatchState.SECOND_SET); break;
            case 3: score.setMatchState(VolleyballMatchState.THIRD_SET); break;
            case 4: score.setMatchState(VolleyballMatchState.FOURTH_SET); break;
            case 5: score.setMatchState(VolleyballMatchState.FIFTH_SET); break;
        }
    }
}