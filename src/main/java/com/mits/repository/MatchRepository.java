package com.mits.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mits.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {

}