package com.mits.service;

import java.util.List;
import com.mits.entity.Team;

public interface TeamService {

    Team createTeam(Team team);

    Team getTeamById(Long id);

    List<Team> getAllTeams();

    Team updateTeam(Long id, Team team);

    void deleteTeam(Long id);

}