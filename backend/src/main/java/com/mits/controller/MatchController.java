package com.mits.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Match;
import com.mits.enums.MatchStatus;
import com.mits.service.MatchService;

@RestController
@RequestMapping("/api/matches") // ✅ Public access for viewing
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    // ✅ ENHANCED: Get matches with optional filters
    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sport) {
        
        List<Match> allMatches = matchService.getAllMatches();
        
        // Filter by status if provided
        if (status != null && !status.isEmpty()) {
            allMatches = allMatches.stream()
                    .filter(match -> match.getStatus().name().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }
        
        // Filter by sport name if provided
        if (sport != null && !sport.isEmpty()) {
            allMatches = allMatches.stream()
                    .filter(match -> match.getSport().getSportName().equalsIgnoreCase(sport))
                    .collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(allMatches);
    }

    // Get Match By Id (Public)
    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        Match match = matchService.getMatchById(id);
        return ResponseEntity.ok(match);
    }
}