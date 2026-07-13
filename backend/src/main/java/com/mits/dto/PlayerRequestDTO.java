package com.mits.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PlayerRequestDTO {

    @NotBlank(message = "Player name is required")
    private String playerName;

    @Min(value = 15, message = "Age must be at least 15")
    private int age;

    private String position;

    // ✅ REMOVED: @NotNull. The team is now assigned by the Bulk Controller.
    private Long teamId; 

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
}