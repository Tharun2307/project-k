package com.mits.service.impl.score.badminton;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.BadmintonScore;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.service.score.badminton.BadmintonScoringService;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BadmintonScoringServiceImpl implements BadmintonScoringService {

    private final MatchEventRepository matchEventRepository;
    private final BadmintonScoreRepository badmintonScoreRepository;

    public BadmintonScoringServiceImpl(MatchEventRepository matchEventRepository,
                                       BadmintonScoreRepository badmintonScoreRepository) {
        this.matchEventRepository = matchEventRepository;
        this.badmintonScoreRepository = badmintonScoreRepository;
    }

    @Override
    public void processBadmintonEvent(MatchEvent event, BadmintonScore score) {
        if (score.isMatchOver()) {
            return;
        }

        boolean isTeam1 = event.getTeam() != null && 
                          event.getTeam().getId().equals(score.getMatch().getTeam1().getId());

        String eventType = event.getEventType().toUpperCase();

        // Standard event types: POINT, ACE, ERROR, FAULT.
        // For simplicity, the selected team in the umpire board is the scoring team.
        if (isTeam1) {
            score.setPlayer1Points(score.getPlayer1Points() + 1);
        } else {
            score.setPlayer2Points(score.getPlayer2Points() + 1);
        }

        checkAndResolveSetWin(score);
    }

    private void checkAndResolveSetWin(BadmintonScore score) {
        int p1 = score.getPlayer1Points();
        int p2 = score.getPlayer2Points();

        boolean p1WinsSet = false;
        boolean p2WinsSet = false;

        // First to 21 points, must lead by 2. Cap at 30 points.
        if (p1 >= 21 && (p1 - p2) >= 2) p1WinsSet = true;
        else if (p1 == 30) p1WinsSet = true;

        if (p2 >= 21 && (p2 - p1) >= 2) p2WinsSet = true;
        else if (p2 == 30) p2WinsSet = true;

        if (p1WinsSet) {
            score.setPlayer1SetsWon(score.getPlayer1SetsWon() + 1);
            resetForNextSet(score);
        } else if (p2WinsSet) {
            score.setPlayer2SetsWon(score.getPlayer2SetsWon() + 1);
            resetForNextSet(score);
        }

        // Best of 3: First to 2 sets
        if (score.getPlayer1SetsWon() == 2 || score.getPlayer2SetsWon() == 2) {
            score.setMatchOver(true);
        }
    }

    private void resetForNextSet(BadmintonScore score) {
        score.setPlayer1Points(0);
        score.setPlayer2Points(0);
        score.setCurrentSet(score.getCurrentSet() + 1);
    }

    @Override
    public void recalculateScore(Match match) {
        BadmintonScore score = badmintonScoreRepository.findByMatch(match)
                .orElse(new BadmintonScore());

        score.setPlayer1SetsWon(0);
        score.setPlayer2SetsWon(0);
        score.setCurrentSet(1);
        score.setPlayer1Points(0);
        score.setPlayer2Points(0);
        score.setMatchOver(false);

        List<MatchEvent> events = matchEventRepository.findByMatchOrderByTimestampAsc(match);
        for (MatchEvent event : events) {
            processBadmintonEvent(event, score);
        }
        badmintonScoreRepository.save(score);
    }
}
