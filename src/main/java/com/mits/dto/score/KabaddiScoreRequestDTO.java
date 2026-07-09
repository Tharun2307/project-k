package com.mits.dto.score; 

import jakarta.validation.constraints.NotNull;

public class KabaddiScoreRequestDTO {

    @NotNull(message = "Match ID is required")
    private Long matchId;

    private int team1Points;
    private int team2Points;
    private int team1Raids;
    private int team2Raids;
    private int team1BonusPoints;
    private int team2BonusPoints;
    private int team1SuperTackles;
    private int team2SuperTackles;

    public KabaddiScoreRequestDTO() {}

    // --- Getters and Setters ---
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public int getTeam1Points() { return team1Points; }
    public void setTeam1Points(int team1Points) { this.team1Points = team1Points; }

    public int getTeam2Points() { return team2Points; }
    public void setTeam2Points(int team2Points) { this.team2Points = team2Points; }

    public int getTeam1Raids() { return team1Raids; }
    public void setTeam1Raids(int team1Raids) { this.team1Raids = team1Raids; }

    public int getTeam2Raids() { return team2Raids; }
    public void setTeam2Raids(int team2Raids) { this.team2Raids = team2Raids; }

    public int getTeam1BonusPoints() { return team1BonusPoints; }
    public void setTeam1BonusPoints(int team1BonusPoints) { this.team1BonusPoints = team1BonusPoints; }

    public int getTeam2BonusPoints() { return team2BonusPoints; }
    public void setTeam2BonusPoints(int team2BonusPoints) { this.team2BonusPoints = team2BonusPoints; }

    public int getTeam1SuperTackles() { return team1SuperTackles; }
    public void setTeam1SuperTackles(int team1SuperTackles) { this.team1SuperTackles = team1SuperTackles; }

    public int getTeam2SuperTackles() { return team2SuperTackles; }
    public void setTeam2SuperTackles(int team2SuperTackles) { this.team2SuperTackles = team2SuperTackles; }
}