package com.mits.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mits.entity.MatchEvent;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {

}