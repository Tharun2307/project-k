package com.mits.entity.score;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "volleyball_scores")
public class VolleyballScore extends Score {

    @Column(nullable = false)
    private int team1SetsWon;

    @Column(nullable = false)
    private int team2SetsWon;

    @Column(nullable = false)
    private int currentSet; // Starts at 1, goes up to 5

    @Column(nullable = false)
    private int team1Points; // Points in the CURRENT set

    @Column(nullable = false)
    private int team2Points; // Points in the CURRENT set

    // --- NEW PROFESSIONAL FIELDS ---

    // Each team gets 2 timeouts per set
    @Column(nullable = false)
    private int team1TimeoutsRemaining = 2;

    @Column(nullable = false)
    private int team2TimeoutsRemaining = 2;

    // Tracks which team is currently serving (1 for Team 1, 2 for Team 2)
    @Column(nullable = false)
    private int servingTeam = 1; 

    // Flag to easily check if the match has been won (Best of 5 means first to 3 sets)
    @Column(nullable = false)
    private boolean matchOver = false;

    public VolleyballScore() {
        // Default initialization for new matches
        this.currentSet = 1;
        this.team1TimeoutsRemaining = 2;
        this.team2TimeoutsRemaining = 2;
        this.servingTeam = 1;
    }

    public VolleyballScore(Long id, int team1SetsWon, int team2SetsWon,
                           int currentSet, int team1Points, int team2Points) {
        setId(id);
        this.team1SetsWon = team1SetsWon;
        this.team2SetsWon = team2SetsWon;
        this.currentSet = currentSet;
        this.team1Points = team1Points;
        this.team2Points = team2Points;
        this.team1TimeoutsRemaining = 2;
        this.team2TimeoutsRemaining = 2;
        this.servingTeam = 1;
    }

    // --- Getters and Setters for Original Fields ---

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

    // --- Getters and Setters for NEW Fields ---

    public int getTeam1TimeoutsRemaining() { return team1TimeoutsRemaining; }
    public void setTeam1TimeoutsRemaining(int team1TimeoutsRemaining) { this.team1TimeoutsRemaining = team1TimeoutsRemaining; }

    public int getTeam2TimeoutsRemaining() { return team2TimeoutsRemaining; }
    public void setTeam2TimeoutsRemaining(int team2TimeoutsRemaining) { this.team2TimeoutsRemaining = team2TimeoutsRemaining; }

    public int getServingTeam() { return servingTeam; }
    public void setServingTeam(int servingTeam) { this.servingTeam = servingTeam; }

    public boolean isMatchOver() { return matchOver; }
    public void setMatchOver(boolean matchOver) { this.matchOver = matchOver; }
}