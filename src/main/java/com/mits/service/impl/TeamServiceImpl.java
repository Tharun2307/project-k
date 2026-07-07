package com.mits.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.Team;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.TeamRepository;
import com.mits.service.TeamService;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public Team getTeamById(Long id) {
        // ✅ Throws 404 if not found
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team updateTeam(Long id, Team team) {
        // ✅ Throws 404 if not found
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));

        existingTeam.setTeamName(team.getTeamName());
        // existingTeam.setSport(team.getSport()); // Uncomment if you allow updating the sport

        return teamRepository.save(existingTeam);
    }

    @Override
    public void deleteTeam(Long id) {
        // ✅ Throws 404 if trying to delete a non-existent team
        if (!teamRepository.existsById(id)) {
            throw new ResourceNotFoundException("Team", "id", id);
        }
        teamRepository.deleteById(id);
    }
}