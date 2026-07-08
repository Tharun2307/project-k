package com.mits.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.mits.entity.Player;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.PlayerRepository;
import com.mits.service.PlayerService;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player updatePlayer(Long id, Player player) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", "id", id));

        existingPlayer.setPlayerName(player.getPlayerName());
        existingPlayer.setAge(player.getAge());
        existingPlayer.setPosition(player.getPosition());
        existingPlayer.setTeam(player.getTeam()); // ✅ Allows updating the team

        return playerRepository.save(existingPlayer);
    }

    @Override
    public void deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Player", "id", id);
        }
        playerRepository.deleteById(id);
    }
}