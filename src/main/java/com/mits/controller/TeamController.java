package com.mits.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Team;
import com.mits.service.TeamService;
import com.mits.dto.TeamRequestDTO;
import com.mits.entity.Sport;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.SportRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final SportRepository sportRepository;

    public TeamController(TeamService teamService, SportRepository sportRepository) {
        this.teamService = teamService;
        this.sportRepository = sportRepository;
    }

    @PostMapping
    public ResponseEntity<Team> createTeam(@Valid @RequestBody TeamRequestDTO dto) {
        // Fetch sport, create team, and save
        Team team = new Team();
        team.setTeamName(dto.getTeamName());
        // Fetch sport from database using dto.getSportId()
        Team savedTeam = teamService.createTeam(team);
        return ResponseEntity.ok(savedTeam);
    }
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Team team = teamService.getTeamById(id);

        if (team == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(team);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id,
                                           @RequestBody Team team) {

        Team updatedTeam = teamService.updateTeam(id, team);

        if (updatedTeam == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok("Team deleted successfully.");
    }
}