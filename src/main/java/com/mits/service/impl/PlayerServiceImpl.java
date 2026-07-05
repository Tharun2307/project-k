package com.mits.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.Player;
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
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player updatePlayer(Long id, Player player) {

        Player existingPlayer = playerRepository.findById(id).orElse(null);

        if (existingPlayer != null) {

            existingPlayer.setPlayerName(player.getPlayerName());
            existingPlayer.setAge(player.getAge());
            existingPlayer.setPosition(player.getPosition());
            existingPlayer.setTeam(player.getTeam());

            return playerRepository.save(existingPlayer);
        }

        return null;
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}