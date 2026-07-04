package com.mits.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mits.entity.Sport;
public interface SportRepository extends JpaRepository<Sport,Long> {
	Optional<Sport> findBySportName(String sportName);

    boolean existsBySportName(String sportName);

}
