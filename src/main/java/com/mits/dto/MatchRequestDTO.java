package com.mits.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime; // ✅ Use LocalDateTime

public class MatchRequestDTO {

    @NotNull(message = "Sport ID is required")
    private Long sportId;

    @NotNull(message = "Team 1 ID is required")
    private Long team1Id;

    @NotNull(message = "Team 2 ID is required")
    private Long team2Id;

    @NotNull(message = "Match date is required")
    private LocalDateTime matchDate; // ✅ Changed to LocalDateTime

    @Pattern(regexp = "UPCOMING|LIVE|COMPLETED|CANCELLED", 
             message = "Status must be UPCOMING, LIVE, COMPLETED, or CANCELLED")
    private String status;

    public MatchRequestDTO() {
    }

    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public Long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(Long team1Id) {
        this.team1Id = team1Id;
    }

    public Long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(Long team2Id) {
        this.team2Id = team2Id;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}