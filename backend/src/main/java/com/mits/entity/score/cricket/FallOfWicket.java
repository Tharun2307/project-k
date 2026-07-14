package com.mits.entity.score.cricket;

import jakarta.persistence.Embeddable;

@Embeddable
public class FallOfWicket {
    
    private int score;
    private String overs;
    private String batsmanName;

    public FallOfWicket() {
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getOvers() { return overs; }
    public void setOvers(String overs) { this.overs = overs; }

    public String getBatsmanName() { return batsmanName; }
    public void setBatsmanName(String batsmanName) { this.batsmanName = batsmanName; }
}