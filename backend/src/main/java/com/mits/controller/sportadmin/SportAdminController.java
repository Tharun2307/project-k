package com.mits.controller.sportadmin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mits.entity.Match;
import com.mits.entity.User;
import com.mits.repository.UserRepository;
import com.mits.service.MatchService;

@RestController
@RequestMapping("/sport-admin")
public class SportAdminController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private UserRepository userRepository;

    // ✅ NEW: Endpoint for Sport Admin to see only their assigned matches
    @GetMapping("/my-matches")
    public ResponseEntity<List<Match>> getMyAssignedMatches() {
        
        // 1. Get the logged-in user's email from the JWT token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // 2. Find the user in the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Get all matches from the database
        List<Match> allMatches = matchService.getAllMatches();

        // 4. Filter matches to ONLY include the sports assigned to this admin
        List<Match> myMatches = allMatches.stream()
                .filter(match -> user.getAssignedSports().contains(match.getSport()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(myMatches);
    }
    // ✅ NEW: Sport Admin can update the status of their assigned match
    @PatchMapping("/{matchId}/status")
    public ResponseEntity<Match> updateMatchStatus(
            @PathVariable Long matchId,
            @RequestParam String status) {

        // 1. Get the logged-in user's email
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Fetch the match
        Match match = matchService.getMatchById(matchId);

        // 3. Security Check: Is this admin assigned to the sport of this match?
        if (!user.getAssignedSports().contains(match.getSport())) {
            throw new RuntimeException("Access Denied: You are not assigned to this sport.");
        }

        // 4. Update the status (UPCOMING -> LIVE -> COMPLETED)
        match.setStatus(com.mits.enums.MatchStatus.valueOf(status.toUpperCase()));
        
        Match updatedMatch = matchService.updateMatch(matchId, match);
        return ResponseEntity.ok(updatedMatch);
    }
}