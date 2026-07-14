package com.mits.service.impl.score;

import com.mits.entity.MatchEvent;
import com.mits.entity.score.KabaddiScore;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.service.score.ScoreUpdateService;
import com.mits.service.score.kabaddi.KabaddiScoreUpdateService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ScoreUpdateServiceImpl implements ScoreUpdateService {

    private final KabaddiScoreRepository kabaddiScoreRepository;
    private final KabaddiScoreUpdateService kabaddiScoreUpdateService;

    public ScoreUpdateServiceImpl(
            KabaddiScoreRepository kabaddiScoreRepository,
            KabaddiScoreUpdateService kabaddiScoreUpdateService) {
        this.kabaddiScoreRepository = kabaddiScoreRepository;
        this.kabaddiScoreUpdateService = kabaddiScoreUpdateService;
    }

    @Override
    public void updateScore(MatchEvent event) {
        String sport = event.getMatch().getSport().getSportName().toUpperCase();

        if ("KABADDI".equals(sport)) {
            Optional<KabaddiScore> kabaddiScoreOpt = kabaddiScoreRepository.findByMatch(event.getMatch());
            kabaddiScoreOpt.ifPresent(score -> {
                // Determine if the team that triggered the event is Team 1 or Team 2
                // NOTE: Adjust '.getTeam1()' if your Match entity uses a different field name (e.g., getHomeTeam())
                boolean isTeam1 = event.getMatch().getTeam1().getId().equals(event.getTeam().getId());
                kabaddiScoreUpdateService.processEvent(score, event, isTeam1);
            });
        }
        // Add Cricket/Volleyball cases here similarly
    }

    public void reverseScore(MatchEvent event) {
        String sport = event.getMatch().getSport().getSportName().toUpperCase();
        if ("KABADDI".equals(sport)) {
            Optional<KabaddiScore> kabaddiScoreOpt = kabaddiScoreRepository.findByMatch(event.getMatch());
            kabaddiScoreOpt.ifPresent(score -> {
                boolean isTeam1 = event.getMatch().getTeam1().getId().equals(event.getTeam().getId());
                kabaddiScoreUpdateService.reverseEvent(score, event, isTeam1);
            });
        }
    }
}