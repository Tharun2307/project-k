package com.mits.service.impl.score;

import org.springframework.stereotype.Service;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.CricketScore;
import com.mits.entity.score.VolleyballScore;
import com.mits.entity.score.KabaddiScore;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.service.ScoreBroadcastService;
import com.mits.service.score.ScoreUpdateService;
import com.mits.service.score.cricket.CricketScoringService;
import com.mits.service.score.volleyball.VolleyballScoringLogicService;
import com.mits.service.score.kabaddi.KabaddiScoreUpdateService; // ✅ NEW: Import Kabaddi Service

import java.util.Optional;

@Service
public class ScoreUpdateServiceImpl implements ScoreUpdateService {

    private final CricketScoreRepository cricketScoreRepository;
    private final VolleyballScoreRepository volleyballScoreRepository;
    private final KabaddiScoreRepository kabaddiScoreRepository;
    private final BadmintonScoreRepository badmintonScoreRepository;
    
    private final CricketScoringService cricketScoringService;
    private final VolleyballScoringLogicService volleyballScoringLogicService;
    private final KabaddiScoreUpdateService kabaddiScoreUpdateService; // ✅ NEW: Inject Kabaddi Service
    
    private final ScoreBroadcastService scoreBroadcastService;

    public ScoreUpdateServiceImpl(CricketScoreRepository cricketScoreRepository,
                                  VolleyballScoreRepository volleyballScoreRepository,
                                  KabaddiScoreRepository kabaddiScoreRepository,
                                  BadmintonScoreRepository badmintonScoreRepository,
                                  CricketScoringService cricketScoringService,
                                  VolleyballScoringLogicService volleyballScoringLogicService,
                                  KabaddiScoreUpdateService kabaddiScoreUpdateService, // ✅ NEW: Add to constructor
                                  ScoreBroadcastService scoreBroadcastService) {
        this.cricketScoreRepository = cricketScoreRepository;
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.kabaddiScoreRepository = kabaddiScoreRepository;
        this.badmintonScoreRepository = badmintonScoreRepository;
        this.cricketScoringService = cricketScoringService;
        this.volleyballScoringLogicService = volleyballScoringLogicService;
        this.kabaddiScoreUpdateService = kabaddiScoreUpdateService; // ✅ NEW: Assign
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
                
            case "VOLLEYBALL":
                Optional<VolleyballScore> volleyballScore = volleyballScoreRepository.findByMatch(event.getMatch());
                volleyballScore.ifPresent(score -> volleyballScoringLogicService.processVolleyballEvent(event, score));
                break;
                
            case "KABADDI":
                Optional<KabaddiScore> kabaddiScore = kabaddiScoreRepository.findByMatch(event.getMatch());
                kabaddiScore.ifPresent(score -> {
                    // 1. Determine if the scoring team is Team 1
                    boolean isTeam1 = event.getTeam() != null && 
                                      event.getTeam().getId().equals(event.getMatch().getTeam1().getId());
                    
                    // 2. Process the event using Member 3's pure logic
                    kabaddiScoreUpdateService.processEvent(score, event, isTeam1);
                    
                    // 3. Save the updated score to the database (Handled by Team Lead to keep Member 3's code clean)
                    kabaddiScoreRepository.save(score);
                });
                break;
                
            case "BADMINTON":
                // Badminton processing service is pending.
                System.out.println("Badminton event recorded. Awaiting BadmintonScoringService implementation.");
                break;
                
            default:
                System.out.println("Unknown sport: " + sport);
                break;
        }

        // Broadcast the update to all connected WebSocket clients (Frontend)
        scoreBroadcastService.broadcastScoreUpdate(event.getMatch().getId(), event);
    }
}