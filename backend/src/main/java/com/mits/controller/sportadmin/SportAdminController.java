package com.mits.controller.sportadmin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Match;
import com.mits.entity.User;
import com.mits.enums.MatchStatus;
import com.mits.repository.UserRepository;
import com.mits.service.MatchService;
import com.mits.service.ScoreBroadcastService; // ✅ ADDED

@RestController
@RequestMapping("/sport-admin")
public class SportAdminController {

    private final MatchService matchService;
    private final UserRepository userRepository;
    private final ScoreBroadcastService scoreBroadcastService; // ✅ ADDED

    public SportAdminController(MatchService matchService, 
                                UserRepository userRepository,
                                ScoreBroadcastService scoreBroadcastService) { // ✅ ADDED
        this.matchService = matchService;
        this.userRepository = userRepository;
        this.scoreBroadcastService = scoreBroadcastService;
    }

    @GetMapping("/my-matches")
    public ResponseEntity<List<Match>> getMyAssignedMatches() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Match> allMatches = matchService.getAllMatches();
        List<Match> myMatches = allMatches.stream()
                .filter(match -> user.getAssignedSports().contains(match.getSport()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(myMatches);
    }

    // ✅ UPDATED: Now broadcasts the status change
    @PatchMapping("/{matchId}/status")
    public ResponseEntity<Match> updateMatchStatus(
            @PathVariable Long matchId,
            @RequestParam String status) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Match match = matchService.getMatchById(matchId);

        if (!user.getAssignedSports().contains(match.getSport())) {
            throw new RuntimeException("Access Denied: You are not assigned to this sport.");
        }

        match.setStatus(MatchStatus.valueOf(status.toUpperCase()));
        Match updatedMatch = matchService.updateMatch(matchId, match);

        // ✅ BROADCAST: Let the frontend know the match is now LIVE/COMPLETED
        scoreBroadcastService.broadcastScoreUpdate(matchId, updatedMatch);
        
        return ResponseEntity.ok(updatedMatch);
    }
}