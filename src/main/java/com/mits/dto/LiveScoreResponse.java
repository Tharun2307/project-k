package com.mits.dto;

import java.util.Map;

public class LiveScoreResponse {

    private Long matchId;
    private String sportName;
    private String team1Name;
    private String team2Name;
    private String status;
    
    // This map will hold the dynamic sport-specific scores
    private Map<String, Object> scoreDetails; 

    public LiveScoreResponse() {
    }

    // --- Explicit Getters and Setters ---

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getScoreDetails() {
        return scoreDetails;
    }

    public void setScoreDetails(Map<String, Object> scoreDetails) {
        this.scoreDetails = scoreDetails;
    }
}