package com.mits.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.MatchService;

@RestController
@RequestMapping("/api/matches") // ✅ Public access
public class LiveScoreController {

    private final MatchService matchService;
    private final CricketScoreRepository cricketScoreRepository;
    private final VolleyballScoreRepository volleyballScoreRepository;
    private final KabaddiScoreRepository kabaddiScoreRepository;
    private final BadmintonScoreRepository badmintonScoreRepository;
    private final MatchEventRepository matchEventRepository;

    public LiveScoreController(MatchService matchService,
                               CricketScoreRepository cricketScoreRepository,
                               VolleyballScoreRepository volleyballScoreRepository,
                               KabaddiScoreRepository kabaddiScoreRepository,
                               BadmintonScoreRepository badmintonScoreRepository,MatchEventRepository matchEventRepository) {
        this.matchService = matchService;
        this.cricketScoreRepository = cricketScoreRepository;
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.kabaddiScoreRepository = kabaddiScoreRepository;
        this.badmintonScoreRepository = badmintonScoreRepository;
        this.matchEventRepository=matchEventRepository;
    }

    // ✅ Public endpoint to get the live score of any match
    @GetMapping("/{matchId}/score")
    public ResponseEntity<?> getLiveScore(@PathVariable Long matchId) {
        
        // 1. Fetch the match to find out which sport it is
        Match match = matchService.getMatchById(matchId);
        String sportName = match.getSport().getSportName().toUpperCase();

        // 2. Fetch the score from the correct table based on the sport
        switch (sportName) {
            case "CRICKET":
                return ResponseEntity.ok(cricketScoreRepository.findByMatch(match).orElse(null));
            case "VOLLEYBALL":
                return ResponseEntity.ok(volleyballScoreRepository.findByMatch(match).orElse(null));
            case "KABADDI":
                return ResponseEntity.ok(kabaddiScoreRepository.findByMatch(match).orElse(null));
            case "BADMINTON":
                return ResponseEntity.ok(badmintonScoreRepository.findByMatch(match).orElse(null));
            default:
                return ResponseEntity.badRequest().body("Unknown sport type");
        }
    }
    // ✅ NEW: Get recent events for momentum tracking
    @GetMapping("/{matchId}/recent-events")
    public ResponseEntity<List<MatchEvent>> getRecentEvents(@PathVariable Long matchId) {
        // Fetch last 10 events ordered by newest first
        List<MatchEvent> events = matchEventRepository.findByMatchIdOrderByTimestampDesc(matchId);
        
        // Return only the first 10
        if (events.size() > 10) {
            return ResponseEntity.ok(events.subList(0, 10));
        }
        return ResponseEntity.ok(events);
    }
}