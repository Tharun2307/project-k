package com.mits.service.impl.score;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.BadmintonScore;
import com.mits.entity.score.CricketScore;
import com.mits.entity.score.KabaddiScore;
import com.mits.entity.score.VolleyballScore;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.ScoreBroadcastService;
import com.mits.service.score.ScoreUpdateService;
import com.mits.service.score.badminton.BadmintonScoringService;
import com.mits.service.score.cricket.CricketScoringService;
import com.mits.service.score.kabaddi.KabaddiScoreUpdateService;
import com.mits.service.score.volleyball.VolleyballScoringLogicService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ScoreUpdateServiceImpl implements ScoreUpdateService {

    private final CricketScoreRepository cricketScoreRepository;
    private final KabaddiScoreRepository kabaddiScoreRepository;
    private final BadmintonScoreRepository badmintonScoreRepository;
    private final VolleyballScoreRepository volleyballScoreRepository;
    
    private final CricketScoringService cricketScoringService;
    private final KabaddiScoreUpdateService kabaddiScoreUpdateService;
    private final BadmintonScoringService badmintonScoringService;
    private final VolleyballScoringLogicService volleyballScoringLogicService;
    
    private final ScoreBroadcastService scoreBroadcastService;

    public ScoreUpdateServiceImpl(CricketScoreRepository cricketScoreRepository,
                                  KabaddiScoreRepository kabaddiScoreRepository,
                                  BadmintonScoreRepository badmintonScoreRepository,
                                  VolleyballScoreRepository volleyballScoreRepository,
                                  CricketScoringService cricketScoringService,
                                  KabaddiScoreUpdateService kabaddiScoreUpdateService,
                                  BadmintonScoringService badmintonScoringService,
                                  VolleyballScoringLogicService volleyballScoringLogicService,
                                  ScoreBroadcastService scoreBroadcastService) {
        this.cricketScoreRepository = cricketScoreRepository;
        this.kabaddiScoreRepository = kabaddiScoreRepository;
        this.badmintonScoreRepository = badmintonScoreRepository;
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.cricketScoringService = cricketScoringService;
        this.kabaddiScoreUpdateService = kabaddiScoreUpdateService;
        this.badmintonScoringService = badmintonScoringService;
        this.volleyballScoringLogicService = volleyballScoringLogicService;
        this.scoreBroadcastService = scoreBroadcastService;
    }

    @Override
    public void updateScore(MatchEvent event) {
        String sport = event.getMatch().getSport().getSportName().toUpperCase();

        switch (sport) {
            case "CRICKET":
                Optional<CricketScore> cricketScore = cricketScoreRepository.findByMatch(event.getMatch());
                cricketScore.ifPresent(score -> cricketScoringService.processCricketEvent(event, score));
                break;
                
            case "KABADDI":
                Optional<KabaddiScore> kabaddiScore = kabaddiScoreRepository.findByMatch(event.getMatch());
                kabaddiScore.ifPresent(score -> {
                    boolean isTeam1 = event.getMatch().getTeam1().getId().equals(event.getTeam().getId());
                    kabaddiScoreUpdateService.processEvent(score, event, isTeam1);
                    kabaddiScoreRepository.save(score);
                });
                break;

            case "BADMINTON":
                Optional<BadmintonScore> badmintonScore = badmintonScoreRepository.findByMatch(event.getMatch());
                badmintonScore.ifPresent(score -> badmintonScoringService.processBadmintonEvent(event, score));
                break;

            case "VOLLEYBALL":
                Optional<VolleyballScore> volleyballScore = volleyballScoreRepository.findByMatch(event.getMatch());
                volleyballScore.ifPresent(score -> volleyballScoringLogicService.processVolleyballEvent(event, score));
                break;
                
            default:
                break;
        }

        scoreBroadcastService.broadcastScoreUpdate(event.getMatch().getId(), event);
    }

    @Override
    public void recalculateScore(Match match) {
        String sport = match.getSport().getSportName().toUpperCase();
        switch (sport) {
            case "CRICKET":
                cricketScoringService.recalculateScore(match);
                break;
            case "BADMINTON":
                badmintonScoringService.recalculateScore(match);
                break;
            case "VOLLEYBALL":
                volleyballScoringLogicService.recalculateScore(match);
                break;
            case "KABADDI":
                // Kabaddi uses reverseEvent logic as previously implemented
                break;
        }
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