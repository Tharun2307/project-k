package com.mits.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Team;
import com.mits.entity.Sport;
import com.mits.service.TeamService;
import com.mits.dto.TeamRequestDTO;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.SportRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final SportRepository sportRepository; // Needed to fetch the Sport entity

    public TeamController(TeamService teamService, SportRepository sportRepository) {
        this.teamService = teamService;
        this.sportRepository = sportRepository;
    }

    // Create Team
    @PostMapping
    public ResponseEntity<Team> createTeam(@Valid @RequestBody TeamRequestDTO dto) {
        Team team = new Team();
        team.setTeamName(dto.getTeamName());

        // Fetch the actual Sport entity from the database using the ID provided in the DTO
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", dto.getSportId()));
        
        team.setSport(sport);

        Team savedTeam = teamService.createTeam(team);
        return ResponseEntity.ok(savedTeam);
    }

    // Get All Teams
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    // Get Team by ID
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Team team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    // Update Team
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamRequestDTO dto) {
        Team existingTeam = teamService.getTeamById(id);
        existingTeam.setTeamName(dto.getTeamName());

        // Update the sport if a new sportId is provided
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", dto.getSportId()));
        existingTeam.setSport(sport);

        Team updatedTeam = teamService.updateTeam(id, existingTeam);
        return ResponseEntity.ok(updatedTeam);
    }

    // Delete Team
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok("Team deleted successfully.");
    }
}