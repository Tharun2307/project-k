package com.mits.dto.score.badminton;

public class BadmintonScorecardDTO {

    private Long matchId;
    private String team1Name;
    private String team2Name;
    private int team1SetsWon;
    private int team2SetsWon;
    private int currentSet;
    private int team1Points;
    private int team2Points;
    private boolean matchOver;
    private String matchStatus;

    public BadmintonScorecardDTO() {
    }

    public BadmintonScorecardDTO(Long matchId, String team1Name, String team2Name, 
                                 int team1SetsWon, int team2SetsWon, int currentSet, 
                                 int team1Points, int team2Points, boolean matchOver, String matchStatus) {
        this.matchId = matchId;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.team1SetsWon = team1SetsWon;
        this.team2SetsWon = team2SetsWon;
        this.currentSet = currentSet;
        this.team1Points = team1Points;
        this.team2Points = team2Points;
        this.matchOver = matchOver;
        this.matchStatus = matchStatus;
    }

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

    public boolean isMatchOver() { return matchOver; }
    public void setMatchOver(boolean matchOver) { this.matchOver = matchOver; }

    public String getMatchStatus() { return matchStatus; }
    public void setMatchStatus(String matchStatus) { this.matchStatus = matchStatus; }
}
