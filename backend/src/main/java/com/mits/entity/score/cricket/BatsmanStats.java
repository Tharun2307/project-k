package com.mits.entity.score.cricket;

import com.mits.enums.DismissalType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class BatsmanStats {
    
    private Long playerId;
    private String playerName;
    private int runs;
    private int balls;
    private int fours;
    private int sixes;
    private double strikeRate;
    
    @Enumerated(EnumType.STRING)
    private DismissalType dismissalType = DismissalType.NOT_OUT;
    
    private Long dismissedByPlayerId;
    private boolean isOut;

    public BatsmanStats() {
    }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getRuns() { return runs; }
    public void setRuns(int runs) { this.runs = runs; }

    public int getBalls() { return balls; }
    public void setBalls(int balls) { this.balls = balls; }

    public int getFours() { return fours; }
    public void setFours(int fours) { this.fours = fours; }

    public int getSixes() { return sixes; }
    public void setSixes(int sixes) { this.sixes = sixes; }

    public double getStrikeRate() { return strikeRate; }
    public void setStrikeRate(double strikeRate) { this.strikeRate = strikeRate; }

    public DismissalType getDismissalType() { return dismissalType; }
    public void setDismissalType(DismissalType dismissalType) { this.dismissalType = dismissalType; }

    public Long getDismissedByPlayerId() { return dismissedByPlayerId; }
    public void setDismissedByPlayerId(Long dismissedByPlayerId) { this.dismissedByPlayerId = dismissedByPlayerId; }

    public boolean isOut() { return isOut; }
    public void setOut(boolean out) { this.isOut = out; }
}