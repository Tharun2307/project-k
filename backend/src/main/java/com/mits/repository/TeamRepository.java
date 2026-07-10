package com.mits.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mits.entity.Sport;
import com.mits.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findBySport(Sport sport);

    boolean existsByTeamName(String teamName);
}