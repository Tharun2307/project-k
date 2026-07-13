package com.mits.controller.sportadmin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.mits.dto.MatchEventRequestDTO;
import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.User;
import com.mits.enums.MatchStatus;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.UserRepository;
import com.mits.service.MatchEventService;
import com.mits.service.MatchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sport-admin/match-events")
public class MatchEventController {

    private final MatchEventService matchEventService;
    private final MatchService matchService;
    private final UserRepository userRepository;
    private final MatchEventRepository matchEventRepository;

    public MatchEventController(MatchEventService matchEventService, 
                                MatchService matchService, 
                                UserRepository userRepository,
                                MatchEventRepository matchEventRepository) {
        this.matchEventService = matchEventService;
        this.matchService = matchService;
        this.userRepository = userRepository;
        this.matchEventRepository = matchEventRepository;
    }

    // ✅ 1. Record a new event (FOUR, WICKET, POINT, etc.)
    @PostMapping
    public ResponseEntity<MatchEvent> recordMatchEvent(@Valid @RequestBody MatchEventRequestDTO dto) {
        
        // Get logged-in Sport Admin
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the match
        Match match = matchService.getMatchById(dto.getMatchId());

        // Security Check: Is admin assigned to this match's sport?
        if (!admin.getAssignedSports().contains(match.getSport())) {
            throw new RuntimeException("Access Denied: You are not assigned to this sport.");
        }

        // Status Check: Is the match actually LIVE?
        if (match.getStatus() != MatchStatus.LIVE) {
            throw new RuntimeException("Cannot update score: Match is not LIVE.");
        }

        // Pass to service to save the event, update the specific sport's score, and broadcast via WebSocket
        MatchEvent savedEvent = matchEventService.recordEvent(dto, admin);
        
        return ResponseEntity.ok(savedEvent);
    }

    // ✅ 2. Undo/Delete the last event (Umpire mistake correction)
    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> undoLastEvent(@PathVariable Long eventId) {
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MatchEvent event = matchEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Match match = event.getMatch();

        // Security Check
        if (!admin.getAssignedSports().contains(match.getSport())) {
            throw new RuntimeException("Access Denied: You are not assigned to this sport.");
        }

        // Delete the event from history
        matchEventRepository.delete(event);
        
        return ResponseEntity.ok("Event deleted successfully. (Note: Manual score adjustment in DB may be required until reverse-logic is fully implemented).");
    }
}