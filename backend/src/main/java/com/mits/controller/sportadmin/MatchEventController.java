package com.mits.controller.sportadmin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.mits.dto.MatchEventRequestDTO;
import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.User;
import com.mits.enums.MatchStatus;
import com.mits.repository.UserRepository;
import com.mits.service.MatchEventService;
import com.mits.service.MatchService;
import com.mits.repository.MatchEventRepository;
import com.mits.service.ScoreBroadcastService;
import com.mits.service.score.ScoreUpdateService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sport-admin/match-events")
public class MatchEventController {

    private final MatchEventService matchEventService;
    private final MatchService matchService;
    private final UserRepository userRepository;
    private final MatchEventRepository matchEventRepository;
    private final ScoreUpdateService scoreUpdateService;
    private final ScoreBroadcastService scoreBroadcastService;

    public MatchEventController(MatchEventService matchEventService, 
                                MatchService matchService, 
                                UserRepository userRepository,
                                MatchEventRepository matchEventRepository,
                                ScoreUpdateService scoreUpdateService,
                                ScoreBroadcastService scoreBroadcastService) {
        this.matchEventService = matchEventService;
        this.matchService = matchService;
        this.userRepository = userRepository;
        this.matchEventRepository = matchEventRepository;
        this.scoreUpdateService = scoreUpdateService;
        this.scoreBroadcastService = scoreBroadcastService;
    }

    @PostMapping
    public ResponseEntity<MatchEvent> recordMatchEvent(@Valid @RequestBody MatchEventRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Match match = matchService.getMatchById(dto.getMatchId());

        if (!admin.getAssignedSports().contains(match.getSport())) {
            throw new RuntimeException("Access Denied: You are not assigned to this sport.");
        }

        if (match.getStatus() != MatchStatus.LIVE) {
            throw new RuntimeException("Cannot update score: Match is not LIVE.");
        }

        MatchEvent savedEvent = matchEventService.recordEvent(dto, admin);
        return ResponseEntity.ok(savedEvent);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> undoLastEvent(@PathVariable Long eventId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MatchEvent event = matchEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Match match = event.getMatch();

        if (!admin.getAssignedSports().contains(match.getSport())) {
            throw new RuntimeException("Access Denied");
        }

        // ✅ REVERSE THE SCORE LOGIC BEFORE DELETING
        scoreUpdateService.reverseScore(event);
        
        // Delete the event from history
        matchEventRepository.delete(event);
        
        // Notify frontend that score was reverted
        scoreBroadcastService.broadcastScoreUpdate(match.getId(), "SCORE_REVERTED");
        
        return ResponseEntity.ok("Event undone and score successfully reverted.");
    }
}