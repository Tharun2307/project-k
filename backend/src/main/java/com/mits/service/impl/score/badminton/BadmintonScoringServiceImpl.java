package com.mits.service.impl.score.badminton;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.BadmintonScore;
import com.mits.enums.BadmintonMatchState;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.service.score.badminton.BadmintonScoringService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BadmintonScoringServiceImpl implements BadmintonScoringService {

    private final BadmintonScoreRepository badmintonScoreRepository;
    private final MatchEventRepository matchEventRepository;

    public BadmintonScoringServiceImpl(BadmintonScoreRepository badmintonScoreRepository, MatchEventRepository matchEventRepository) {
        this.badmintonScoreRepository = badmintonScoreRepository;
        this.matchEventRepository = matchEventRepository;
    }

    @Override
    public void processBadmintonEvent(MatchEvent event, BadmintonScore score) {
        processEventInternal(score, event);
        badmintonScoreRepository.save(score);
    }

    @Override
    public void recalculateScore(Match match) {
        BadmintonScore score = badmintonScoreRepository.findByMatch(match).orElse(new BadmintonScore());
        resetScore(score);
        
        List<MatchEvent> events = matchEventRepository.findByMatchOrderByTimestampAsc(match);
        for (MatchEvent event : events) {
            processEventInternal(score, event);
        }
        badmintonScoreRepository.save(score);
    }

    private void resetScore(BadmintonScore score) {
        score.setPlayer1SetsWon(0);
        score.setPlayer2SetsWon(0);
        score.setCurrentSet(1);
        score.setPlayer1Points(0);
        score.setPlayer2Points(0);
        score.setMatchState(BadmintonMatchState.FIRST_GAME);
        score.setCurrentServerId(null);
        score.setPlayer1IntervalReached(false);
        score.setPlayer2IntervalReached(false);
        score.setRallyHistory(new ArrayList<>());
        score.setResult(null);
    }

    private void processEventInternal(BadmintonScore score, MatchEvent event) {
        if (score.getMatchState() == BadmintonMatchState.COMPLETED) {
            return; // Ignore events after match completion
        }

        boolean isTeam1 = event.getMatch().getTeam1().getId().equals(event.getTeam().getId());
        String eventType = event.getEventType().toUpperCase();
        
        // In badminton, any of these events means the scoring side won the rally
        if (eventType.matches("POINT|ACE|ERROR|FAULT|OUT")) {
            if (isTeam1) {
                score.setPlayer1Points(score.getPlayer1Points() + 1);
                score.setCurrentServerId(event.getPlayer() != null ? event.getPlayer().getId() : event.getMatch().getTeam1().getId());
            } else {
                score.setPlayer2Points(score.getPlayer2Points() + 1);
                score.setCurrentServerId(event.getPlayer() != null ? event.getPlayer().getId() : event.getMatch().getTeam2().getId());
            }

            // Update rally history (keep last 10)
            score.getRallyHistory().add(isTeam1 ? "P1" : "P2");
            if (score.getRallyHistory().size() > 10) {
                score.getRallyHistory().remove(0);
            }

            // Check Interval (11 points)
            if (isTeam1 && score.getPlayer1Points() == 11 && !score.isPlayer1IntervalReached()) {
                score.setPlayer1IntervalReached(true);
            } else if (!isTeam1 && score.getPlayer2Points() == 11 && !score.isPlayer2IntervalReached()) {
                score.setPlayer2IntervalReached(true);
            }

            // Check Game Win
            checkGameWin(score, isTeam1);
        }
    }

    private void checkGameWin(BadmintonScore score, boolean isTeam1Winner) {
        int winnerPoints = isTeam1Winner ? score.getPlayer1Points() : score.getPlayer2Points();
        int loserPoints = isTeam1Winner ? score.getPlayer2Points() : score.getPlayer1Points();

        boolean winByTwo = (winnerPoints >= 21) && (winnerPoints - loserPoints >= 2);
        boolean winAtThirty = (winnerPoints == 30);

        if (winByTwo || winAtThirty) {
            if (isTeam1Winner) {
                score.setPlayer1SetsWon(score.getPlayer1SetsWon() + 1);
            } else {
                score.setPlayer2SetsWon(score.getPlayer2SetsWon() + 1);
            }

            int setsWon = isTeam1Winner ? score.getPlayer1SetsWon() : score.getPlayer2SetsWon();

            if (setsWon == 2) {
                score.setMatchState(BadmintonMatchState.COMPLETED);
                score.setResult((isTeam1Winner ? "Player 1" : "Player 2") + " won the match");
            } else {
                // Next game
                score.setCurrentSet(score.getCurrentSet() + 1);
                score.setPlayer1Points(0);
                score.setPlayer2Points(0);
                score.setPlayer1IntervalReached(false);
                score.setPlayer2IntervalReached(false);
                
                if (score.getCurrentSet() == 2) {
                    score.setMatchState(BadmintonMatchState.SECOND_GAME);
                } else if (score.getCurrentSet() == 3) {
                    score.setMatchState(BadmintonMatchState.THIRD_GAME);
                }
            }
        }
    }
}
