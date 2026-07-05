package com.mits.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.MatchEvent;
import com.mits.service.MatchEventService;

@RestController
@RequestMapping("/api/match-events")
public class MatchEventController {

    private final MatchEventService matchEventService;

    public MatchEventController(MatchEventService matchEventService) {
        this.matchEventService = matchEventService;
    }

    // Create Match Event
    @PostMapping
    public ResponseEntity<MatchEvent> createMatchEvent(@RequestBody MatchEvent matchEvent) {
        MatchEvent savedMatchEvent = matchEventService.createMatchEvent(matchEvent);
        return new ResponseEntity<>(savedMatchEvent, HttpStatus.CREATED);
    }

    // Get All Match Events
    @GetMapping
    public ResponseEntity<List<MatchEvent>> getAllMatchEvents() {
        return ResponseEntity.ok(matchEventService.getAllMatchEvents());
    }

    // Get Match Event By Id
    @GetMapping("/{id}")
    public ResponseEntity<MatchEvent> getMatchEventById(@PathVariable Long id) {

        MatchEvent matchEvent = matchEventService.getMatchEventById(id);

        if (matchEvent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(matchEvent);
    }

    // Update Match Event
    @PutMapping("/{id}")
    public ResponseEntity<MatchEvent> updateMatchEvent(@PathVariable Long id,
                                                       @RequestBody MatchEvent matchEvent) {

        MatchEvent updatedMatchEvent =
                matchEventService.updateMatchEvent(id, matchEvent);

        if (updatedMatchEvent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedMatchEvent);
    }

    // Delete Match Event
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatchEvent(@PathVariable Long id) {

        matchEventService.deleteMatchEvent(id);

        return ResponseEntity.ok("Match Event deleted successfully.");
    }
}