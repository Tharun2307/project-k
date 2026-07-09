package com.mits.dto.score; // Change to com.mits.dto if you don't have a score subpackage

import jakarta.validation.constraints.NotNull;

public class VolleyballScoreRequestDTO {

    @NotNull(message = "Match ID is required")
    private Long matchId;

    private int team1SetsWon;
    private int team2SetsWon;
    private int currentSet;
    private int team1Points;
    private int team2Points;

    public VolleyballScoreRequestDTO() {}

    // --- Getters and Setters ---
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public int getTeam1SetsWon() { return team1SetsWon; }
    public void setTeam1SetsWon(int team1SetsWon) { this.team1SetsWon = team1SetsWon; }

    public int getTeam2SetsWon() { return team2SetsWon; }
    public void setTeam2SetsWon(int team2SetsWon) { this.team2SetsWon = team2SetsWon; }

    public int getCurrentSet() { return currentSet; }
    public void setCurrentSet(int currentSet) { this.currentSet = currentSet; }

    public int getTeam1Points() { return team1Points; }
    public void setTeam1Points(int team1Points) { this.team1Points = team1Points; }

    public int getTeam2Points() { return team2Points; }
    public void setTeam2Points(int team2Points) { this.team2Points = team2Points; }
}