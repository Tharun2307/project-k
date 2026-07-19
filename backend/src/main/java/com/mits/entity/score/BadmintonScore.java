package com.mits.entity.score;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "badminton_scores")
public class BadmintonScore extends Score {

    @Column(nullable = false)
    private int player1SetsWon;

    @Column(nullable = false)
    private int player2SetsWon;

    @Column(nullable = false)
    private int currentSet;

    @Column(nullable = false)
    private int player1Points;

    @Column(nullable = false)
    private int player2Points;

    public BadmintonScore() {
    }

    public BadmintonScore(Long id, int player1SetsWon,
                          int player2SetsWon,
                          int currentSet,
                          int player1Points,
                          int player2Points) {

        setId(id);
        this.player1SetsWon = player1SetsWon;
        this.player2SetsWon = player2SetsWon;
        this.currentSet = currentSet;
        this.player1Points = player1Points;
        this.player2Points = player2Points;
    }

    public int getPlayer1SetsWon() {
        return player1SetsWon;
    }

    public void setPlayer1SetsWon(int player1SetsWon) {
        this.player1SetsWon = player1SetsWon;
    }

    public int getPlayer2SetsWon() {
        return player2SetsWon;
    }

    public void setPlayer2SetsWon(int player2SetsWon) {
        this.player2SetsWon = player2SetsWon;
    }

    public int getCurrentSet() {
        return currentSet;
    }

    public void setCurrentSet(int currentSet) {
        this.currentSet = currentSet;
    }

    public int getPlayer1Points() {
        return player1Points;
    }

    public void setPlayer1Points(int player1Points) {
        this.player1Points = player1Points;
    }

    public int getPlayer2Points() {
        return player2Points;
    }

    public void setPlayer2Points(int player2Points) {
        this.player2Points = player2Points;
    }

    @jakarta.persistence.Column(nullable = false)
    private boolean matchOver = false;

    public boolean isMatchOver() {
        return matchOver;
    }

    public void setMatchOver(boolean matchOver) {
        this.matchOver = matchOver;
    }
}