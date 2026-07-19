package com.mits.service.impl.score.volleyball;
import org.springframework.stereotype.Service;
import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.VolleyballScore;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.score.volleyball.VolleyballScoringLogicService;
import java.util.List;

@Service
public class VolleyballScoringLogicServiceImpl implements VolleyballScoringLogicService {

    private final MatchEventRepository matchEventRepository;
    private final VolleyballScoreRepository volleyballScoreRepository;

    public VolleyballScoringLogicServiceImpl(MatchEventRepository matchEventRepository,
                                             VolleyballScoreRepository volleyballScoreRepository) {
        this.matchEventRepository = matchEventRepository;
        this.volleyballScoreRepository = volleyballScoreRepository;
    }

    @Override
    public void processVolleyballEvent(MatchEvent event, VolleyballScore score) {
        if (score.isMatchOver()) {
            return; // Do not process events if the match is already over
        }

        String eventType = event.getEventType().toUpperCase();
        boolean isTeam1 = event.getTeam() != null && 
                          event.getTeam().getId().equals(score.getMatch().getTeam1().getId());
        
        int scoringTeam = isTeam1 ? 1 : 2;

        // 1. Handle Timeouts
        if ("TIMEOUT".equals(eventType)) {
            if (isTeam1 && score.getTeam1TimeoutsRemaining() > 0) {
                score.setTeam1TimeoutsRemaining(score.getTeam1TimeoutsRemaining() - 1);
            } else if (!isTeam1 && score.getTeam2TimeoutsRemaining() > 0) {
                score.setTeam2TimeoutsRemaining(score.getTeam2TimeoutsRemaining() - 1);
            }
            return; // Timeouts don't award points
        }

        // 2. Handle Scoring Events (POINT, ACE, SPIKE, BLOCK, ERROR by opponent)
        if ("POINT".equals(eventType) || "ACE".equals(eventType) || 
            "SPIKE".equals(eventType) || "BLOCK".equals(eventType)) {
            
            // Award point
            if (isTeam1) {
                score.setTeam1Points(score.getTeam1Points() + 1);
            } else {
                score.setTeam2Points(score.getTeam2Points() + 1);
            }

            // The team that wins the point gets the serve
            score.setServingTeam(scoringTeam);

            // 3. Check for Set Win
            checkAndResolveSetWin(score);
        }
    }

    private void checkAndResolveSetWin(VolleyballScore score) {
        int points1 = score.getTeam1Points();
        int points2 = score.getTeam2Points();
        int currentSet = score.getCurrentSet();
        
        boolean team1WinsSet = false;
        boolean team2WinsSet = false;

        // Sets 1 and 2: First to 25 points, must win by 2
        if (currentSet < 3) {
            if (points1 >= 25 && (points1 - points2) >= 2) team1WinsSet = true;
            if (points2 >= 25 && (points2 - points1) >= 2) team2WinsSet = true;
        } 
        // Set 3 (Tiebreaker): First to 15 points, must win by 2
        else {
            if (points1 >= 15 && (points1 - points2) >= 2) team1WinsSet = true;
            if (points2 >= 15 && (points2 - points1) >= 2) team2WinsSet = true;
        }

        // 4. Resolve Set Win
        if (team1WinsSet) {
            score.setTeam1SetsWon(score.getTeam1SetsWon() + 1);
            resetForNextSet(score);
        } else if (team2WinsSet) {
            score.setTeam2SetsWon(score.getTeam2SetsWon() + 1);
            resetForNextSet(score);
        }

        // 5. Check for Match Win (Best of 3: First to 2 sets)
        if (score.getTeam1SetsWon() == 2 || score.getTeam2SetsWon() == 2) {
            score.setMatchOver(true);
        }
    }

    private void resetForNextSet(VolleyballScore score) {
        score.setTeam1Points(0);
        score.setTeam2Points(0);
        score.setCurrentSet(score.getCurrentSet() + 1);
        // Reset timeouts for the new set
        score.setTeam1TimeoutsRemaining(2);
        score.setTeam2TimeoutsRemaining(2);
        // Serving team for the new set can be set to the team that just won, 
        // or you can add a coin toss logic later. Defaulting to the winner of the last set.
    }

    @Override
    public void recalculateScore(Match match) {
        VolleyballScore score = volleyballScoreRepository.findByMatch(match)
                .orElse(new VolleyballScore());
        score.setTeam1SetsWon(0);
        score.setTeam2SetsWon(0);
        score.setCurrentSet(1);
        score.setTeam1Points(0);
        score.setTeam2Points(0);
        score.setTeam1TimeoutsRemaining(2);
        score.setTeam2TimeoutsRemaining(2);
        score.setServingTeam(1);
        score.setMatchOver(false);

        List<MatchEvent> events = matchEventRepository.findByMatchOrderByTimestampAsc(match);
        for (MatchEvent event : events) {
            processVolleyballEvent(event, score);
        }
        volleyballScoreRepository.save(score);
    }
}