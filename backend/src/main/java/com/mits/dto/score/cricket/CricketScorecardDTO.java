package com.mits.dto.score.cricket;

import com.mits.entity.score.cricket.BatsmanStats;
import com.mits.entity.score.cricket.BowlerStats;
import com.mits.entity.score.cricket.FallOfWicket;
import com.mits.enums.MatchState;
import java.util.List;

public class CricketScorecardDTO {
    private String team1Name;
    private String team2Name;
    private String team1Score;
    private String team2Score;
    
    private int team1Runs, team1Wickets, team1Overs, team1Balls;
    private int team2Runs, team2Wickets, team2Overs, team2Balls;
    
    private int target;
    private double currentRunRate;
    private double requiredRunRate;
    private String matchSituation;
    private String result;
    
    // ✅ NEW FIELDS
    private MatchState matchState;
    private int currentInnings;
    private int ballsRemaining;
    private int runsNeeded;
    private int wicketsRemaining;
    private int partnershipRuns;
    private int partnershipBalls;
    
    private List<BatsmanStats> currentBatsmen;
    private BowlerStats currentBowler;
    private List<FallOfWicket> fallOfWickets;
    private List<String> lastOverBalls;
    private List<String> recentBalls;

    // Getters and Setters for all fields
    public String getTeam1Name() { return team1Name; }
    public void setTeam1Name(String team1Name) { this.team1Name = team1Name; }
    public String getTeam2Name() { return team2Name; }
    public void setTeam2Name(String team2Name) { this.team2Name = team2Name; }
    public String getTeam1Score() { return team1Score; }
    public void setTeam1Score(String team1Score) { this.team1Score = team1Score; }
    public String getTeam2Score() { return team2Score; }
    public void setTeam2Score(String team2Score) { this.team2Score = team2Score; }
    public int getTeam1Runs() { return team1Runs; }
    public void setTeam1Runs(int team1Runs) { this.team1Runs = team1Runs; }
    public int getTeam1Wickets() { return team1Wickets; }
    public void setTeam1Wickets(int team1Wickets) { this.team1Wickets = team1Wickets; }
    public int getTeam1Overs() { return team1Overs; }
    public void setTeam1Overs(int team1Overs) { this.team1Overs = team1Overs; }
    public int getTeam1Balls() { return team1Balls; }
    public void setTeam1Balls(int team1Balls) { this.team1Balls = team1Balls; }
    public int getTeam2Runs() { return team2Runs; }
    public void setTeam2Runs(int team2Runs) { this.team2Runs = team2Runs; }
    public int getTeam2Wickets() { return team2Wickets; }
    public void setTeam2Wickets(int team2Wickets) { this.team2Wickets = team2Wickets; }
    public int getTeam2Overs() { return team2Overs; }
    public void setTeam2Overs(int team2Overs) { this.team2Overs = team2Overs; }
    public int getTeam2Balls() { return team2Balls; }
    public void setTeam2Balls(int team2Balls) { this.team2Balls = team2Balls; }
    public int getTarget() { return target; }
    public void setTarget(int target) { this.target = target; }
    public double getCurrentRunRate() { return currentRunRate; }
    public void setCurrentRunRate(double currentRunRate) { this.currentRunRate = currentRunRate; }
    public double getRequiredRunRate() { return requiredRunRate; }
    public void setRequiredRunRate(double requiredRunRate) { this.requiredRunRate = requiredRunRate; }
    public String getMatchSituation() { return matchSituation; }
    public void setMatchSituation(String matchSituation) { this.matchSituation = matchSituation; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public MatchState getMatchState() { return matchState; }
    public void setMatchState(MatchState matchState) { this.matchState = matchState; }
    public int getCurrentInnings() { return currentInnings; }
    public void setCurrentInnings(int currentInnings) { this.currentInnings = currentInnings; }
    public int getBallsRemaining() { return ballsRemaining; }
    public void setBallsRemaining(int ballsRemaining) { this.ballsRemaining = ballsRemaining; }
    public int getRunsNeeded() { return runsNeeded; }
    public void setRunsNeeded(int runsNeeded) { this.runsNeeded = runsNeeded; }
    public int getWicketsRemaining() { return wicketsRemaining; }
    public void setWicketsRemaining(int wicketsRemaining) { this.wicketsRemaining = wicketsRemaining; }
    public int getPartnershipRuns() { return partnershipRuns; }
    public void setPartnershipRuns(int partnershipRuns) { this.partnershipRuns = partnershipRuns; }
    public int getPartnershipBalls() { return partnershipBalls; }
    public void setPartnershipBalls(int partnershipBalls) { this.partnershipBalls = partnershipBalls; }
    public List<BatsmanStats> getCurrentBatsmen() { return currentBatsmen; }
    public void setCurrentBatsmen(List<BatsmanStats> currentBatsmen) { this.currentBatsmen = currentBatsmen; }
    public BowlerStats getCurrentBowler() { return currentBowler; }
    public void setCurrentBowler(BowlerStats currentBowler) { this.currentBowler = currentBowler; }
    public List<FallOfWicket> getFallOfWickets() { return fallOfWickets; }
    public void setFallOfWickets(List<FallOfWicket> fallOfWickets) { this.fallOfWickets = fallOfWickets; }
    public List<String> getLastOverBalls() { return lastOverBalls; }
    public void setLastOverBalls(List<String> lastOverBalls) { this.lastOverBalls = lastOverBalls; }
    public List<String> getRecentBalls() { return recentBalls; }
    public void setRecentBalls(List<String> recentBalls) { this.recentBalls = recentBalls; }
}