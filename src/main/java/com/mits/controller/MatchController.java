package com.mits.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Match;
import com.mits.service.MatchService;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    // Create Match
    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        Match savedMatch = matchService.createMatch(match);
        return new ResponseEntity<>(savedMatch, HttpStatus.CREATED);
    }

    // Get All Matches
    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    // Get Match By Id
    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {

        Match match = matchService.getMatchById(id);

        if (match == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(match);
    }

    // Update Match
    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long id,
                                             @RequestBody Match match) {

        Match updatedMatch = matchService.updateMatch(id, match);

        if (updatedMatch == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedMatch);
    }

    // Delete Match
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatch(@PathVariable Long id) {

        matchService.deleteMatch(id);

        return ResponseEntity.ok("Match deleted successfully.");
    }
}