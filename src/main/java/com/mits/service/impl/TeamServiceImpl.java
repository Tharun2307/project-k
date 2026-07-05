package com.mits.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.Team;
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
        return teamRepository.findById(id).orElse(null);
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team updateTeam(Long id, Team team) {

        Team existingTeam = teamRepository.findById(id).orElse(null);

        if (existingTeam != null) {
            existingTeam.setTeamName(team.getTeamName());
           
            existingTeam.setSport(team.getSport());

            return teamRepository.save(existingTeam);
        }

        return null;
    }

    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }
}