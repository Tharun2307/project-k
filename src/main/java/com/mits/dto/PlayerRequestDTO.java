package com.mits.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlayerRequestDTO {

    @NotBlank(message = "Player name is required")
    @Size(min = 2, max = 100, message = "Player name must be between 2 and 100 characters")
    private String playerName;

    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 100, message = "Age must not exceed 100")
    private int age;

    @Size(max = 50, message = "Position must not exceed 50 characters")
    private String position;

    @NotNull(message = "Team ID is required")
    private Long teamId;

    public PlayerRequestDTO() {}

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
}