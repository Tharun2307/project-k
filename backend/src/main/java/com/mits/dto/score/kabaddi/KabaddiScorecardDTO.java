package com.mits.dto.score.kabaddi;

public class KabaddiScorecardDTO {

    private Long matchId;
    private String team1Name;
    private String team2Name;
    
    // Team 1 Detailed Stats
    private int team1TotalPoints;
    private int team1RaidPoints;
    private int team1TacklePoints;
    private int team1BonusPoints;
    private int team1SuperTackles;
    private int team1AllOuts;
    
    // Team 2 Detailed Stats
    private int team2TotalPoints;
    private int team2RaidPoints;
    private int team2TacklePoints;
    private int team2BonusPoints;
    private int team2SuperTackles;
    private int team2AllOuts;

    // Derived Stats (Calculated by logic)
    private int team1DefendersRemaining;
    private int team2DefendersRemaining;
    private String currentLead;
    private int currentHalf;

    // Explicit Constructor
    public KabaddiScorecardDTO(Long matchId, String team1Name, String team2Name,
                               int team1TotalPoints, int team1RaidPoints, int team1TacklePoints,
                               int team1BonusPoints, int team1SuperTackles, int team1AllOuts,
                               int team2TotalPoints, int team2RaidPoints, int team2TacklePoints,
                               int team2BonusPoints, int team2SuperTackles, int team2AllOuts,
                               int team1DefendersRemaining, int team2DefendersRemaining, String currentLead, int currentHalf) {
        this.matchId = matchId;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.team1TotalPoints = team1TotalPoints;
        this.team1RaidPoints = team1RaidPoints;
        this.team1TacklePoints = team1TacklePoints;
        this.team1BonusPoints = team1BonusPoints;
        this.team1SuperTackles = team1SuperTackles;
        this.team1AllOuts = team1AllOuts;
        this.team2TotalPoints = team2TotalPoints;
        this.team2RaidPoints = team2RaidPoints;
        this.team2TacklePoints = team2TacklePoints;
        this.team2BonusPoints = team2BonusPoints;
        this.team2SuperTackles = team2SuperTackles;
        this.team2AllOuts = team2AllOuts;
        this.team1DefendersRemaining = team1DefendersRemaining;
        this.team2DefendersRemaining = team2DefendersRemaining;
        this.currentLead = currentLead;
        this.currentHalf = currentHalf;
    }

    // Explicit Getters
    public Long getMatchId() { return matchId; }
    public String getTeam1Name() { return team1Name; }
    public String getTeam2Name() { return team2Name; }
    public int getTeam1TotalPoints() { return team1TotalPoints; }
    public int getTeam1RaidPoints() { return team1RaidPoints; }
    public int getTeam1TacklePoints() { return team1TacklePoints; }
    public int getTeam1BonusPoints() { return team1BonusPoints; }
    public int getTeam1SuperTackles() { return team1SuperTackles; }
    public int getTeam1AllOuts() { return team1AllOuts; }
    public int getTeam2TotalPoints() { return team2TotalPoints; }
    public int getTeam2RaidPoints() { return team2RaidPoints; }
    public int getTeam2TacklePoints() { return team2TacklePoints; }
    public int getTeam2BonusPoints() { return team2BonusPoints; }
    public int getTeam2SuperTackles() { return team2SuperTackles; }
    public int getTeam2AllOuts() { return team2AllOuts; }
    public int getTeam1DefendersRemaining() { return team1DefendersRemaining; }
    public int getTeam2DefendersRemaining() { return team2DefendersRemaining; }
    public String getCurrentLead() { return currentLead; }
    public int getCurrentHalf() { return currentHalf; }
}