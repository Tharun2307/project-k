package com.mits.service;

import java.util.List;
import com.mits.entity.Player;

public interface PlayerService {
    Player createPlayer(Player player);
    Player getPlayerById(Long id);
    List<Player> getAllPlayers();
    Player updatePlayer(Long id, Player player);
    void deletePlayer(Long id);
}