package com.mits.entity.score;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "kabaddi_scores")
public class KabaddiScore extends Score {

    @Column(nullable = false)
    private int team1Points;

    @Column(nullable = false)
    private int team2Points;

    @Column(nullable = false)
    private int team1Raids;

    @Column(nullable = false)
    private int team2Raids;

    @Column(nullable = false)
    private int team1BonusPoints;

    @Column(nullable = false)
    private int team2BonusPoints;

    @Column(nullable = false)
    private int team1SuperTackles;

    @Column(nullable = false)
    private int team2SuperTackles;

    // --- NEW FIELDS ADDED FOR PROFESSIONAL SCORECARD ---
    @Column(nullable = false)
    private int team1TacklePoints;

    @Column(nullable = false)
    private int team2TacklePoints;

    @Column(nullable = false)
    private int team1AllOuts;

    @Column(nullable = false)
    private int team2AllOuts;
    // ---------------------------------------------------

    public KabaddiScore() {
    }

    public KabaddiScore(Long id, int team1Points, int team2Points,
                        int team1Raids, int team2Raids,
                        int team1BonusPoints, int team2BonusPoints,
                        int team1SuperTackles, int team2SuperTackles,
                        int team1TacklePoints, int team2TacklePoints,
                        int team1AllOuts, int team2AllOuts) {

        setId(id);
        this.team1Points = team1Points;
        this.team2Points = team2Points;
        this.team1Raids = team1Raids;
        this.team2Raids = team2Raids;
        this.team1BonusPoints = team1BonusPoints;
        this.team2BonusPoints = team2BonusPoints;
        this.team1SuperTackles = team1SuperTackles;
        this.team2SuperTackles = team2SuperTackles;
        this.team1TacklePoints = team1TacklePoints;
        this.team2TacklePoints = team2TacklePoints;
        this.team1AllOuts = team1AllOuts;
        this.team2AllOuts = team2AllOuts;
    }

    // --- Existing Getters and Setters ---
    public int getTeam1Points() { return team1Points; }
    public void setTeam1Points(int team1Points) { this.team1Points = team1Points; }

    public int getTeam2Points() { return team2Points; }
    public void setTeam2Points(int team2Points) { this.team2Points = team2Points; }

    public int getTeam1Raids() { return team1Raids; }
    public void setTeam1Raids(int team1Raids) { this.team1Raids = team1Raids; }

    public int getTeam2Raids() { return team2Raids; }
    public void setTeam2Raids(int team2Raids) { this.team2Raids = team2Raids; }

    public int getTeam1BonusPoints() { return team1BonusPoints; }
    public void setTeam1BonusPoints(int team1BonusPoints) { this.team1BonusPoints = team1BonusPoints; }

    public int getTeam2BonusPoints() { return team2BonusPoints; }
    public void setTeam2BonusPoints(int team2BonusPoints) { this.team2BonusPoints = team2BonusPoints; }

    public int getTeam1SuperTackles() { return team1SuperTackles; }
    public void setTeam1SuperTackles(int team1SuperTackles) { this.team1SuperTackles = team1SuperTackles; }

    public int getTeam2SuperTackles() { return team2SuperTackles; }
    public void setTeam2SuperTackles(int team2SuperTackles) { this.team2SuperTackles = team2SuperTackles; }

    // --- NEW Getters and Setters ---
    public int getTeam1TacklePoints() { return team1TacklePoints; }
    public void setTeam1TacklePoints(int team1TacklePoints) { this.team1TacklePoints = team1TacklePoints; }

    public int getTeam2TacklePoints() { return team2TacklePoints; }
    public void setTeam2TacklePoints(int team2TacklePoints) { this.team2TacklePoints = team2TacklePoints; }

    public int getTeam1AllOuts() { return team1AllOuts; }
    public void setTeam1AllOuts(int team1AllOuts) { this.team1AllOuts = team1AllOuts; }

    public int getTeam2AllOuts() { return team2AllOuts; }
    public void setTeam2AllOuts(int team2AllOuts) { this.team2AllOuts = team2AllOuts; }
}