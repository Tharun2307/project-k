package com.mits.dto.score; 

import jakarta.validation.constraints.NotNull;

public class BadmintonScoreRequestDTO {

    @NotNull(message = "Match ID is required")
    private Long matchId;

    private int player1SetsWon;
    private int player2SetsWon;
    private int currentSet;
    private int player1Points;
    private int player2Points;

    public BadmintonScoreRequestDTO() {}

    // --- Getters and Setters ---
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

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
}