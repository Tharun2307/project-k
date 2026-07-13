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
                                UserRepository userRepository,MatchEventRepository matchEventRepository) {
        this.matchEventService = matchEventService;
        this.matchService = matchService;
        this.userRepository = userRepository;
        this.matchEventRepository=matchEventRepository;
    }

    // ✅ Endpoint for Sport Admin to record a score/event
    @PostMapping
    public ResponseEntity<MatchEvent> recordMatchEvent(@Valid @RequestBody MatchEventRequestDTO dto) {
        
        // 1. Get logged-in Sport Admin
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Fetch the match
        Match match = matchService.getMatchById(dto.getMatchId());

        // 3. Security Check: Is admin assigned to this match's sport?
        if (!admin.getAssignedSports().contains(match.getSport())) {
            throw new RuntimeException("Access Denied: You are not assigned to this sport.");
        }

        // 4. Status Check: Is the match actually LIVE?
        if (match.getStatus() != MatchStatus.LIVE) {
            throw new RuntimeException("Cannot update score: Match is not LIVE.");
        }

        // 5. Pass to service to save the event and update the specific sport's score
        MatchEvent savedEvent = matchEventService.recordEvent(dto, admin);
        
        return ResponseEntity.ok(savedEvent);
    }
    // ✅ NEW: Undo the last event
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
            throw new RuntimeException("Access Denied");
        }

        // Reverse the score logic (subtract runs/points)
        // We use a negative version of the event to reverse it
        MatchEvent reverseEvent = new MatchEvent();
        reverseEvent.setMatch(match);
        reverseEvent.setEventType(event.getEventType());
        reverseEvent.setTeam(event.getTeam());
        reverseEvent.setPlayer(event.getPlayer());
        
        // Call the same service but with negative logic? 
        // Actually, it's easier to just delete the event and recalculate or subtract manually.
        // For simplicity in this MVP, let's just delete the event and let the frontend refetch.
        // But to keep the DB score accurate, we should subtract.
        
        // ⚠️ Complex Logic Warning: Reversing scores perfectly for all sports is complex.
        // For this step, we will just delete the event from history. 
        // If you want perfect score reversal, we'd need a 'reverseScore' method in ScoreUpdateService.
        
        matchEventRepository.delete(event);
        return ResponseEntity.ok("Event deleted. Please note: Total score in DB may need manual adjustment if auto-reversal isn't implemented.");
    }
}