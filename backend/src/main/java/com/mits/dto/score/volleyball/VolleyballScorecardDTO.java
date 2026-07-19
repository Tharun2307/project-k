package com.mits.dto.score.volleyball;

import com.mits.enums.VolleyballMatchState;
import java.util.List;

public class VolleyballScorecardDTO {

    private Long matchId;
    private String team1Name;
    private String team2Name;
    
    private int team1SetsWon;
    private int team2SetsWon;
    
    private int currentSet;
    private int team1Points;
    private int team2Points;
    
    private int team1TimeoutsRemaining;
    private int team2TimeoutsRemaining;
    
    private int servingTeam; // 1 or 2
    private boolean matchOver;
    private String matchStatus; // e.g., "LIVE", "COMPLETED"
    
    private VolleyballMatchState matchState;
    private List<String> setHistory; // e.g., ["25-22", "20-25"]
    private List<String> rallyHistory; // Last 10 rallies
    private int sideSwitches;
    private String result;

    // --- Getters and Setters ---
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public String getTeam1Name() { return team1Name; }
    public void setTeam1Name(String team1Name) { this.team1Name = team1Name; }

    public String getTeam2Name() { return team2Name; }
    public void setTeam2Name(String team2Name) { this.team2Name = team2Name; }

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

    public int getTeam1TimeoutsRemaining() { return team1TimeoutsRemaining; }
    public void setTeam1TimeoutsRemaining(int team1TimeoutsRemaining) { this.team1TimeoutsRemaining = team1TimeoutsRemaining; }

    public int getTeam2TimeoutsRemaining() { return team2TimeoutsRemaining; }
    public void setTeam2TimeoutsRemaining(int team2TimeoutsRemaining) { this.team2TimeoutsRemaining = team2TimeoutsRemaining; }

    public int getServingTeam() { return servingTeam; }
    public void setServingTeam(int servingTeam) { this.servingTeam = servingTeam; }

    public boolean isMatchOver() { return matchOver; }
    public void setMatchOver(boolean matchOver) { this.matchOver = matchOver; }

    public String getMatchStatus() { return matchStatus; }
    public void setMatchStatus(String matchStatus) { this.matchStatus = matchStatus; }

    public VolleyballMatchState getMatchState() { return matchState; }
    public void setMatchState(VolleyballMatchState matchState) { this.matchState = matchState; }

    public List<String> getSetHistory() { return setHistory; }
    public void setSetHistory(List<String> setHistory) { this.setHistory = setHistory; }

    public List<String> getRallyHistory() { return rallyHistory; }
    public void setRallyHistory(List<String> rallyHistory) { this.rallyHistory = rallyHistory; }

    public int getSideSwitches() { return sideSwitches; }
    public void setSideSwitches(int sideSwitches) { this.sideSwitches = sideSwitches; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}