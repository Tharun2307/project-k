package com.mits.entity.score;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cricket_scores")
public class CricketScore extends Score {

    @Column(nullable = false)
    private int runs;

    @Column(nullable = false)
    private int wickets;

    @Column(nullable = false)
    private String overs;

    @Column(nullable = false)
    private int extras;

    private Integer target;

    @Column(nullable = false)
    private int innings;

    public CricketScore() {
    }

    public CricketScore(Long id, int runs, int wickets, String overs,
                        int extras, Integer target, int innings) {
        setId(id);
        this.runs = runs;
        this.wickets = wickets;
        this.overs = overs;
        this.extras = extras;
        this.target = target;
        this.innings = innings;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public String getOvers() {
        return overs;
    }

    public void setOvers(String overs) {
        this.overs = overs;
    }

    public int getExtras() {
        return extras;
    }

    public void setExtras(int extras) {
        this.extras = extras;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }
}