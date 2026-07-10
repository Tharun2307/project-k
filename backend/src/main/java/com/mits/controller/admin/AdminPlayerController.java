package com.mits.controller.admin;

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
@RequestMapping("/admin/players") // ✅ Protected by EVENT_COORDINATOR role
public class AdminPlayerController {

    private final PlayerService playerService;
    private final TeamRepository teamRepository;

    public AdminPlayerController(PlayerService playerService, TeamRepository teamRepository) {
        this.playerService = playerService;
        this.teamRepository = teamRepository;
    }

    // Create Player (Event Coordinator Only)
    @PostMapping
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody PlayerRequestDTO dto) {
        Player player = new Player();
        player.setPlayerName(dto.getPlayerName());
        player.setAge(dto.getAge());
        player.setPosition(dto.getPosition());
        
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeamId()));
        player.setTeam(team);

        Player savedPlayer = playerService.createPlayer(player);
        return ResponseEntity.ok(savedPlayer);
    }

    // Update Player (Event Coordinator Only)
    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerRequestDTO dto) {
        Player existingPlayer = playerService.getPlayerById(id);
        
        existingPlayer.setPlayerName(dto.getPlayerName());
        existingPlayer.setAge(dto.getAge());
        existingPlayer.setPosition(dto.getPosition());
        
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeamId()));
        existingPlayer.setTeam(team);

        Player updatedPlayer = playerService.updatePlayer(id, existingPlayer);
        return ResponseEntity.ok(updatedPlayer);
    }

    // Delete Player (Event Coordinator Only)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.ok("Player deleted successfully.");
    }
}