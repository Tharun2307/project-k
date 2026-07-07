package com.mits.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.Sport;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.SportRepository;
import com.mits.service.SportService;

@Service
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;

    public SportServiceImpl(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @Override
    public Sport createSport(Sport sport) {
        return sportRepository.save(sport);
    }

    @Override
    public Sport getSportById(Long id) {
        // ✅ Throws 404 if not found
        return sportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", id));
    }

    @Override
    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }

    @Override
    public Sport updateSport(Long id, Sport sport) {
        // ✅ Throws 404 if not found
        Sport existingSport = sportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", id));

        existingSport.setSportName(sport.getSportName());
        existingSport.setDescription(sport.getDescription());

        return sportRepository.save(existingSport);
    }

    @Override
    public void deleteSport(Long id) {
        // ✅ Throws 404 if trying to delete a non-existent sport
        if (!sportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sport", "id", id);
        }
        sportRepository.deleteById(id);
    }
}