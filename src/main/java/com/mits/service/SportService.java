package com.mits.service;

import java.util.List;

import com.mits.entity.Sport;

public interface SportService {

    Sport createSport(Sport sport);

    Sport getSportById(Long id);

    List<Sport> getAllSports();

    Sport updateSport(Long id, Sport sport);

    void deleteSport(Long id);

}