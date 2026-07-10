package com.mits.controller.admin;

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
@RequestMapping("/admin/teams") // ✅ Protected by EVENT_COORDINATOR role
public class AdminTeamController {

    private final TeamService teamService;
    private final SportRepository sportRepository;

    public AdminTeamController(TeamService teamService, SportRepository sportRepository) {
        this.teamService = teamService;
        this.sportRepository = sportRepository;
    }

    // Create Team (Event Coordinator Only)
    @PostMapping
    public ResponseEntity<Team> createTeam(@Valid @RequestBody TeamRequestDTO dto) {
        Team team = new Team();
        team.setTeamName(dto.getTeamName());

        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", dto.getSportId()));
        
        team.setSport(sport);

        Team savedTeam = teamService.createTeam(team);
        return ResponseEntity.ok(savedTeam);
    }

    // Update Team (Event Coordinator Only)
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamRequestDTO dto) {
        Team existingTeam = teamService.getTeamById(id);
        existingTeam.setTeamName(dto.getTeamName());

        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", dto.getSportId()));
        existingTeam.setSport(sport);

        Team updatedTeam = teamService.updateTeam(id, existingTeam);
        return ResponseEntity.ok(updatedTeam);
    }

    // Delete Team (Event Coordinator Only)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok("Team deleted successfully.");
    }
}