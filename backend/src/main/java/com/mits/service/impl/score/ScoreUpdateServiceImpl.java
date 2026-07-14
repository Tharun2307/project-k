package com.mits.service.impl.score;

import com.mits.entity.MatchEvent;
import com.mits.entity.score.CricketScore;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.service.ScoreBroadcastService;
import com.mits.service.score.ScoreUpdateService;
import com.mits.service.score.cricket.CricketScoringService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ScoreUpdateServiceImpl implements ScoreUpdateService {

    private final CricketScoreRepository cricketScoreRepository;
    private final VolleyballScoreRepository volleyballScoreRepository;
    private final KabaddiScoreRepository kabaddiScoreRepository;
    private final BadmintonScoreRepository badmintonScoreRepository;
    private final CricketScoringService cricketScoringService;
    private final ScoreBroadcastService scoreBroadcastService;

    public ScoreUpdateServiceImpl(CricketScoreRepository cricketScoreRepository,
                                  VolleyballScoreRepository volleyballScoreRepository,
                                  KabaddiScoreRepository kabaddiScoreRepository,
                                  BadmintonScoreRepository badmintonScoreRepository,
                                  CricketScoringService cricketScoringService,
                                  ScoreBroadcastService scoreBroadcastService) {
        this.cricketScoreRepository = cricketScoreRepository;
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.kabaddiScoreRepository = kabaddiScoreRepository;
        this.badmintonScoreRepository = badmintonScoreRepository;
        this.cricketScoringService = cricketScoringService;
        this.scoreBroadcastService = scoreBroadcastService;
    }

    @Override
    public void updateScore(MatchEvent event) {
        String sport = event.getMatch().getSport().getSportName().toUpperCase();

        if ("CRICKET".equals(sport)) {
            Optional<CricketScore> cricketScore = cricketScoreRepository.findByMatch(event.getMatch());
            cricketScore.ifPresent(score -> cricketScoringService.processCricketEvent(event, score));
        } 
        // Add Volleyball/Kabaddi/Badminton cases here as needed

        scoreBroadcastService.broadcastScoreUpdate(event.getMatch().getId(), event);
    }
}