package com.mits.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mits.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}