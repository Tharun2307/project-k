package com.mits.dto.score.badminton;

import com.mits.enums.BadmintonMatchState;
import java.util.List;

public class BadmintonScorecardDTO {
    private Long matchId;
    private String player1Name;
    private String player2Name;
    
    private int player1SetsWon;
    private int player2SetsWon;
    private int currentSet;
    
    private int player1Points;
    private int player2Points;
    
    private BadmintonMatchState matchState;
    private Long currentServerId;
    private boolean isInterval; // True if someone just reached 11 points
    
    private List<String> rallyHistory; // Last 10 rallies (e.g., ["P1", "P2", "P1"])
    private String result;
    private String matchSituation; // e.g., "First to 21 points (win by 2)"

    // Getters and Setters
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }
    public String getPlayer1Name() { return player1Name; }
    public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; }
    public String getPlayer2Name() { return player2Name; }
    public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; }
    public int getPlayer1SetsWon() { return player1SetsWon; }
    public void setPlayer1SetsWon(int player1SetsWon) { this.player1SetsWon = player1SetsWon; }
    public int getPlayer2SetsWon() { return player2SetsWon; }
    public void setPlayer2SetsWon(int player2SetsWon) { this.player2SetsWon = player2SetsWon; }
    public int getCurrentSet() { return currentSet; }
    public void setCurrentSet(int currentSet) { this.currentSet = currentSet; }
    public int getPlayer1Points() { return player1Points; }
    public void setPlayer1Points(int player1Points) { this.player1Points = player1Points; }
    public int getPlayer2Points() { return player2Points; }
    public void setPlayer2Points(int player2Points) { this.player2Points = player2Points; }
    public BadmintonMatchState getMatchState() { return matchState; }
    public void setMatchState(BadmintonMatchState matchState) { this.matchState = matchState; }
    public Long getCurrentServerId() { return currentServerId; }
    public void setCurrentServerId(Long currentServerId) { this.currentServerId = currentServerId; }
    public boolean isInterval() { return isInterval; }
    public void setInterval(boolean interval) { isInterval = interval; }
    public List<String> getRallyHistory() { return rallyHistory; }
    public void setRallyHistory(List<String> rallyHistory) { this.rallyHistory = rallyHistory; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getMatchSituation() { return matchSituation; }
    public void setMatchSituation(String matchSituation) { this.matchSituation = matchSituation; }
}
