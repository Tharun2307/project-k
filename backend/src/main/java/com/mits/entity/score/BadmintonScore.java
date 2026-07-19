package com.mits.entity.score;

import com.mits.entity.Match;
import com.mits.enums.BadmintonMatchState;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "badminton_scores")
public class BadmintonScore extends Score {

    @Column(nullable = false)
    private int player1SetsWon;

    @Column(nullable = false)
    private int player2SetsWon;

    @Column(nullable = false)
    private int currentSet; // 1, 2, or 3

    @Column(nullable = false)
    private int player1Points;

    @Column(nullable = false)
    private int player2Points;

    // --- NEW ADVANCED FIELDS ---
    @Enumerated(EnumType.STRING)
    private BadmintonMatchState matchState = BadmintonMatchState.FIRST_GAME;

    private Long currentServerId; // Player ID of who is currently serving

    private boolean player1IntervalReached; // Reached 11 points in current game
    private boolean player2IntervalReached;

    @ElementCollection
    @CollectionTable(name = "badminton_score_rally_history", joinColumns = @JoinColumn(name = "score_id"))
    @OrderColumn(name = "rally_index")
    private List<String> rallyHistory = new ArrayList<>();

    private String result; // e.g., "Player 1 won 21-18, 15-21, 21-19"

    public BadmintonScore() {
        this.currentSet = 1;
    }

    public BadmintonScore(Long id, int player1SetsWon, int player2SetsWon, int currentSet, int player1Points, int player2Points) {
        setId(id);
        this.player1SetsWon = player1SetsWon;
        this.player2SetsWon = player2SetsWon;
        this.currentSet = currentSet;
        this.player1Points = player1Points;
        this.player2Points = player2Points;
    }

    // --- GETTERS AND SETTERS ---
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

    public boolean isPlayer1IntervalReached() { return player1IntervalReached; }
    public void setPlayer1IntervalReached(boolean player1IntervalReached) { this.player1IntervalReached = player1IntervalReached; }

    public boolean isPlayer2IntervalReached() { return player2IntervalReached; }
    public void setPlayer2IntervalReached(boolean player2IntervalReached) { this.player2IntervalReached = player2IntervalReached; }

    public List<String> getRallyHistory() { return rallyHistory; }
    public void setRallyHistory(List<String> rallyHistory) { this.rallyHistory = rallyHistory; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public boolean isMatchOver() { return matchState == BadmintonMatchState.COMPLETED; }
}