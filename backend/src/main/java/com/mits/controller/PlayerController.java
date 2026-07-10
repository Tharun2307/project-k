package com.mits.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Player;
import com.mits.entity.Team;
import com.mits.service.PlayerService;
import com.mits.dto.PlayerRequestDTO;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.TeamRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;
    private final TeamRepository teamRepository;

    public PlayerController(PlayerService playerService, TeamRepository teamRepository) {
        this.playerService = playerService;
        this.teamRepository = teamRepository;
    }

    // Create Player
    @PostMapping
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody PlayerRequestDTO dto) {
        Player player = new Player();
        player.setPlayerName(dto.getPlayerName());
        player.setAge(dto.getAge());
        player.setPosition(dto.getPosition());
        
        // ✅ Fetch team from database using dto.getTeamId()
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeamId()));
        player.setTeam(team);

        Player savedPlayer = playerService.createPlayer(player);
        return ResponseEntity.ok(savedPlayer);
    }

    // Get All Players
    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    // Get Player By Id
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        return ResponseEntity.ok(player);
    }

    // Update Player
    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(
            @PathVariable Long id,
            @Valid @RequestBody PlayerRequestDTO dto) {

        Player existingPlayer = playerService.getPlayerById(id);
        
        existingPlayer.setPlayerName(dto.getPlayerName());
        existingPlayer.setAge(dto.getAge());
        existingPlayer.setPosition(dto.getPosition());
        
        // ✅ Update team
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeamId()));
        existingPlayer.setTeam(team);

        Player updatedPlayer = playerService.updatePlayer(id, existingPlayer);
        return ResponseEntity.ok(updatedPlayer);
    }

    // Delete Player
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok("Player deleted successfully.");
    }
}