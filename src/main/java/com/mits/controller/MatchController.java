package com.mits.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Match;
import com.mits.entity.Sport;
import com.mits.entity.Team;
import com.mits.enums.MatchStatus;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.SportRepository;
import com.mits.repository.TeamRepository;
import com.mits.service.MatchService;
import com.mits.dto.MatchRequestDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;
    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;

    public MatchController(MatchService matchService, 
            SportRepository sportRepository, 
            TeamRepository teamRepository) {
        this.matchService = matchService;
        this.sportRepository = sportRepository;
        this.teamRepository = teamRepository;
    }

    // Create Match
    @PostMapping
    public ResponseEntity<Match> createMatch(@Valid @RequestBody MatchRequestDTO dto) {
        Match match = buildMatchFromDto(dto);
        Match savedMatch = matchService.createMatch(match);
        return ResponseEntity.ok(savedMatch);
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
        return ResponseEntity.ok(match);
    }

    // Update Match
    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long id,
                                             @Valid @RequestBody MatchRequestDTO dto) {
        
        // Fetch existing match (Throws 404 if not found)
        Match existingMatch = matchService.getMatchById(id);
        
        // Fetch updated relationships
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", dto.getSportId()));
        Team team1 = teamRepository.findById(dto.getTeam1Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeam1Id()));
        Team team2 = teamRepository.findById(dto.getTeam2Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeam2Id()));

        // Update fields
        existingMatch.setSport(sport);
        existingMatch.setTeam1(team1);
        existingMatch.setTeam2(team2);
        existingMatch.setMatchDate(dto.getMatchDate());
        
        if (dto.getStatus() != null) {
            existingMatch.setStatus(MatchStatus.valueOf(dto.getStatus().toUpperCase()));
        }

        Match updatedMatch = matchService.updateMatch(id, existingMatch);
        return ResponseEntity.ok(updatedMatch);
    }

    // Delete Match
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.ok("Match deleted successfully.");
    }

    // Helper method to avoid duplicating code between Create and Update
    private Match buildMatchFromDto(MatchRequestDTO dto) {
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", dto.getSportId()));

        Team team1 = teamRepository.findById(dto.getTeam1Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeam1Id()));

        Team team2 = teamRepository.findById(dto.getTeam2Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeam2Id()));

        Match match = new Match();
        match.setSport(sport);
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setMatchDate(dto.getMatchDate());

        if (dto.getStatus() != null) {
            match.setStatus(MatchStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        
        return match;
    }
}