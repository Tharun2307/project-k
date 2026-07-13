package com.mits.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BulkPlayerRequestDTO {

    @NotNull(message = "Team ID is required")
    private Long teamId;

    @Valid
    @NotNull(message = "Players list cannot be empty")
    private List<PlayerRequestDTO> players;

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public List<PlayerRequestDTO> getPlayers() { return players; }
    public void setPlayers(List<PlayerRequestDTO> players) { this.players = players; }
}