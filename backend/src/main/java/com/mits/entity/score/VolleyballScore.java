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
    private int currentSet;

    @Column(nullable = false)
    private int team1Points;

    @Column(nullable = false)
    private int team2Points;

    public VolleyballScore() {
    }

    public VolleyballScore(Long id, int team1SetsWon, int team2SetsWon,
                           int currentSet, int team1Points, int team2Points) {
        setId(id);
        this.team1SetsWon = team1SetsWon;
        this.team2SetsWon = team2SetsWon;
        this.currentSet = currentSet;
        this.team1Points = team1Points;
        this.team2Points = team2Points;
    }

    public int getTeam1SetsWon() {
        return team1SetsWon;
    }

    public void setTeam1SetsWon(int team1SetsWon) {
        this.team1SetsWon = team1SetsWon;
    }

    public int getTeam2SetsWon() {
        return team2SetsWon;
    }

    public void setTeam2SetsWon(int team2SetsWon) {
        this.team2SetsWon = team2SetsWon;
    }

    public int getCurrentSet() {
        return currentSet;
    }

    public void setCurrentSet(int currentSet) {
        this.currentSet = currentSet;
    }

    public int getTeam1Points() {
        return team1Points;
    }

    public void setTeam1Points(int team1Points) {
        this.team1Points = team1Points;
    }

    public int getTeam2Points() {
        return team2Points;
    }

    public void setTeam2Points(int team2Points) {
        this.team2Points = team2Points;
    }
}