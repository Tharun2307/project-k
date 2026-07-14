package com.mits.entity.score.cricket;

import jakarta.persistence.Embeddable;

@Embeddable
public class BowlerStats {
    
    private Long playerId;
    private String playerName;
    private int overs;
    private int balls;
    private int runsGiven;
    private int wickets;
    private int maidens;
    private double economy;

    public BowlerStats() {
    }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getOvers() { return overs; }
    public void setOvers(int overs) { this.overs = overs; }

    public int getBalls() { return balls; }
    public void setBalls(int balls) { this.balls = balls; }

    public int getRunsGiven() { return runsGiven; }
    public void setRunsGiven(int runsGiven) { this.runsGiven = runsGiven; }

    public int getWickets() { return wickets; }
    public void setWickets(int wickets) { this.wickets = wickets; }

    public int getMaidens() { return maidens; }
    public void setMaidens(int maidens) { this.maidens = maidens; }

    public double getEconomy() { return economy; }
    public void setEconomy(double economy) { this.economy = economy; }
}