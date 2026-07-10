package com.mits.dto.score; // Change to com.mits.dto if you don't have a score subpackage

import jakarta.validation.constraints.NotNull;

public class CricketScoreRequestDTO {

    @NotNull(message = "Match ID is required")
    private Long matchId;

    private int team1Runs;
    private int team1Wickets;
    private int team2Runs;
    private int team2Wickets;
    private double overs;
    private int extras;
    private int target;
    private int innings;

    public CricketScoreRequestDTO() {}

    // --- Getters and Setters ---
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public int getTeam1Runs() { return team1Runs; }
    public void setTeam1Runs(int team1Runs) { this.team1Runs = team1Runs; }

    public int getTeam1Wickets() { return team1Wickets; }
    public void setTeam1Wickets(int team1Wickets) { this.team1Wickets = team1Wickets; }

    public int getTeam2Runs() { return team2Runs; }
    public void setTeam2Runs(int team2Runs) { this.team2Runs = team2Runs; }

    public int getTeam2Wickets() { return team2Wickets; }
    public void setTeam2Wickets(int team2Wickets) { this.team2Wickets = team2Wickets; }

    public double getOvers() { return overs; }
    public void setOvers(double overs) { this.overs = overs; }

    public int getExtras() { return extras; }
    public void setExtras(int extras) { this.extras = extras; }

    public int getTarget() { return target; }
    public void setTarget(int target) { this.target = target; }

    public int getInnings() { return innings; }
    public void setInnings(int innings) { this.innings = innings; }
}