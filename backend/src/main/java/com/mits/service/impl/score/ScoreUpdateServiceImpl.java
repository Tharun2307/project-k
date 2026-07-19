package com.mits.service.impl.score;

import com.mits.entity.MatchEvent;
import com.mits.entity.score.KabaddiScore;
import com.mits.entity.score.CricketScore;
import com.mits.entity.score.VolleyballScore;
import com.mits.entity.score.BadmintonScore;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.service.score.ScoreUpdateService;
import com.mits.service.score.kabaddi.KabaddiScoreUpdateService;
import com.mits.service.score.cricket.CricketScoringService;
import com.mits.service.score.volleyball.VolleyballScoringLogicService;
import com.mits.service.score.badminton.BadmintonScoringService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ScoreUpdateServiceImpl implements ScoreUpdateService {

    private final KabaddiScoreRepository kabaddiScoreRepository;
    private final KabaddiScoreUpdateService kabaddiScoreUpdateService;
    private final CricketScoreRepository cricketScoreRepository;
    private final CricketScoringService cricketScoringService;
    private final VolleyballScoreRepository volleyballScoreRepository;
    private final VolleyballScoringLogicService volleyballScoringLogicService;
    private final BadmintonScoreRepository badmintonScoreRepository;
    private final BadmintonScoringService badmintonScoringService;

    public ScoreUpdateServiceImpl(
            KabaddiScoreRepository kabaddiScoreRepository,
            KabaddiScoreUpdateService kabaddiScoreUpdateService,
            CricketScoreRepository cricketScoreRepository,
            CricketScoringService cricketScoringService,
            VolleyballScoreRepository volleyballScoreRepository,
            VolleyballScoringLogicService volleyballScoringLogicService,
            BadmintonScoreRepository badmintonScoreRepository,
            BadmintonScoringService badmintonScoringService) {
        this.kabaddiScoreRepository = kabaddiScoreRepository;
        this.kabaddiScoreUpdateService = kabaddiScoreUpdateService;
        this.cricketScoreRepository = cricketScoreRepository;
        this.cricketScoringService = cricketScoringService;
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.volleyballScoringLogicService = volleyballScoringLogicService;
        this.badmintonScoreRepository = badmintonScoreRepository;
        this.badmintonScoringService = badmintonScoringService;
    }

    @Override
    public void updateScore(MatchEvent event) {
        String sport = event.getMatch().getSport().getSportName().toUpperCase();

        if ("KABADDI".equals(sport)) {
            Optional<KabaddiScore> kabaddiScoreOpt = kabaddiScoreRepository.findByMatch(event.getMatch());
            kabaddiScoreOpt.ifPresent(score -> {
                boolean isTeam1 = event.getMatch().getTeam1().getId().equals(event.getTeam().getId());
                kabaddiScoreUpdateService.processEvent(score, event, isTeam1);
            });
        }
        else if ("CRICKET".equals(sport)) {
            Optional<CricketScore> cricketScoreOpt = cricketScoreRepository.findByMatch(event.getMatch());
            cricketScoreOpt.ifPresent(score -> {
                cricketScoringService.processCricketEvent(event, score);
            });
        }
        else if ("VOLLEYBALL".equals(sport)) {
            Optional<VolleyballScore> volleyballScoreOpt = volleyballScoreRepository.findByMatch(event.getMatch());
            volleyballScoreOpt.ifPresent(score -> {
                volleyballScoringLogicService.processVolleyballEvent(event, score);
            });
        }
        else if ("BADMINTON".equals(sport)) {
            Optional<BadmintonScore> badmintonScoreOpt = badmintonScoreRepository.findByMatch(event.getMatch());
            badmintonScoreOpt.ifPresent(score -> {
                badmintonScoringService.processBadmintonEvent(event, score);
            });
        }
    }

    @Override
    public void reverseScore(MatchEvent event) {
        String sport = event.getMatch().getSport().getSportName().toUpperCase();
        
        if ("KABADDI".equals(sport)) {
            Optional<KabaddiScore> kabaddiScoreOpt = kabaddiScoreRepository.findByMatch(event.getMatch());
            kabaddiScoreOpt.ifPresent(score -> {
                boolean isTeam1 = event.getMatch().getTeam1().getId().equals(event.getTeam().getId());
                kabaddiScoreUpdateService.reverseEvent(score, event, isTeam1);
            });
        }
        else if ("CRICKET".equals(sport)) {
            cricketScoringService.recalculateScore(event.getMatch());
        }
        else if ("VOLLEYBALL".equals(sport)) {
            volleyballScoringLogicService.recalculateScore(event.getMatch());
        }
        else if ("BADMINTON".equals(sport)) {
            badmintonScoringService.recalculateScore(event.getMatch());
        }
    }
}