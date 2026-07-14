package com.mits.entity.score;

import com.mits.entity.Match;
import com.mits.entity.score.cricket.BatsmanStats;
import com.mits.entity.score.cricket.BowlerStats;
import com.mits.entity.score.cricket.FallOfWicket;
import com.mits.enums.MatchState;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CricketScore extends Score {

    private int currentInnings;
    private int battingTeam;
    private int totalOversInInnings;

    private int team1Runs, team1Wickets, team1Overs, team1Balls;
    private int team1Extras, team1Wides, team1NoBalls, team1Byes, team1LegByes;

    private int team2Runs, team2Wickets, team2Overs, team2Balls;
    private int team2Extras, team2Wides, team2NoBalls, team2Byes, team2LegByes;

    private int target;
    private double currentRunRate;
    private double requiredRunRate;
    private String result;

    // ✅ NEW FIELDS FOR ADVANCED SCORING
    @Enumerated(EnumType.STRING)
    private MatchState matchState = MatchState.FIRST_INNINGS;

    private int totalLegalBallsTeam1;
    private int totalLegalBallsTeam2;

    private Long currentStrikerId;
    private Long currentNonStrikerId;
    private Long currentBowlerId;

    private int partnershipRuns;
    private int partnershipBalls;

    @ElementCollection
    @CollectionTable(name = "cricket_score_batsman_stats", joinColumns = @JoinColumn(name = "score_id"))
    private List<BatsmanStats> batsmanStatsList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cricket_score_bowler_stats", joinColumns = @JoinColumn(name = "score_id"))
    private List<BowlerStats> bowlerStatsList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cricket_score_fow", joinColumns = @JoinColumn(name = "score_id"))
    @OrderColumn(name = "wicket_number")
    private List<FallOfWicket> fallOfWicketsList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cricket_score_last_over", joinColumns = @JoinColumn(name = "score_id"))
    @OrderColumn(name = "ball_index")
    private List<String> lastOverBalls = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cricket_score_recent_balls", joinColumns = @JoinColumn(name = "score_id"))
    @OrderColumn(name = "ball_index")
    private List<String> recentBalls = new ArrayList<>();

    public CricketScore() {
        this.currentInnings = 1;
        this.battingTeam = 1;
        this.totalOversInInnings = 20;
    }

    // --- GETTERS AND SETTERS FOR ALL FIELDS ---
    public int getCurrentInnings() { return currentInnings; }
    public void setCurrentInnings(int currentInnings) { this.currentInnings = currentInnings; }
    public int getBattingTeam() { return battingTeam; }
    public void setBattingTeam(int battingTeam) { this.battingTeam = battingTeam; }
    public int getTotalOversInInnings() { return totalOversInInnings; }
    public void setTotalOversInInnings(int totalOversInInnings) { this.totalOversInInnings = totalOversInInnings; }

    public int getTeam1Runs() { return team1Runs; }
    public void setTeam1Runs(int team1Runs) { this.team1Runs = team1Runs; }
    public int getTeam1Wickets() { return team1Wickets; }
    public void setTeam1Wickets(int team1Wickets) { this.team1Wickets = team1Wickets; }
    public int getTeam1Overs() { return team1Overs; }
    public void setTeam1Overs(int team1Overs) { this.team1Overs = team1Overs; }
    public int getTeam1Balls() { return team1Balls; }
    public void setTeam1Balls(int team1Balls) { this.team1Balls = team1Balls; }
    public int getTeam1Extras() { return team1Extras; }
    public void setTeam1Extras(int team1Extras) { this.team1Extras = team1Extras; }
    public int getTeam1Wides() { return team1Wides; }
    public void setTeam1Wides(int team1Wides) { this.team1Wides = team1Wides; }
    public int getTeam1NoBalls() { return team1NoBalls; }
    public void setTeam1NoBalls(int team1NoBalls) { this.team1NoBalls = team1NoBalls; }
    public int getTeam1Byes() { return team1Byes; }
    public void setTeam1Byes(int team1Byes) { this.team1Byes = team1Byes; }
    public int getTeam1LegByes() { return team1LegByes; }
    public void setTeam1LegByes(int team1LegByes) { this.team1LegByes = team1LegByes; }

    public int getTeam2Runs() { return team2Runs; }
    public void setTeam2Runs(int team2Runs) { this.team2Runs = team2Runs; }
    public int getTeam2Wickets() { return team2Wickets; }
    public void setTeam2Wickets(int team2Wickets) { this.team2Wickets = team2Wickets; }
    public int getTeam2Overs() { return team2Overs; }
    public void setTeam2Overs(int team2Overs) { this.team2Overs = team2Overs; }
    public int getTeam2Balls() { return team2Balls; }
    public void setTeam2Balls(int team2Balls) { this.team2Balls = team2Balls; }
    public int getTeam2Extras() { return team2Extras; }
    public void setTeam2Extras(int team2Extras) { this.team2Extras = team2Extras; }
    public int getTeam2Wides() { return team2Wides; }
    public void setTeam2Wides(int team2Wides) { this.team2Wides = team2Wides; }
    public int getTeam2NoBalls() { return team2NoBalls; }
    public void setTeam2NoBalls(int team2NoBalls) { this.team2NoBalls = team2NoBalls; }
    public int getTeam2Byes() { return team2Byes; }
    public void setTeam2Byes(int team2Byes) { this.team2Byes = team2Byes; }
    public int getTeam2LegByes() { return team2LegByes; }
    public void setTeam2LegByes(int team2LegByes) { this.team2LegByes = team2LegByes; }

    public int getTarget() { return target; }
    public void setTarget(int target) { this.target = target; }
    public double getCurrentRunRate() { return currentRunRate; }
    public void setCurrentRunRate(double currentRunRate) { this.currentRunRate = currentRunRate; }
    public double getRequiredRunRate() { return requiredRunRate; }
    public void setRequiredRunRate(double requiredRunRate) { this.requiredRunRate = requiredRunRate; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public MatchState getMatchState() { return matchState; }
    public void setMatchState(MatchState matchState) { this.matchState = matchState; }
    public int getTotalLegalBallsTeam1() { return totalLegalBallsTeam1; }
    public void setTotalLegalBallsTeam1(int totalLegalBallsTeam1) { this.totalLegalBallsTeam1 = totalLegalBallsTeam1; }
    public int getTotalLegalBallsTeam2() { return totalLegalBallsTeam2; }
    public void setTotalLegalBallsTeam2(int totalLegalBallsTeam2) { this.totalLegalBallsTeam2 = totalLegalBallsTeam2; }
    public Long getCurrentStrikerId() { return currentStrikerId; }
    public void setCurrentStrikerId(Long currentStrikerId) { this.currentStrikerId = currentStrikerId; }
    public Long getCurrentNonStrikerId() { return currentNonStrikerId; }
    public void setCurrentNonStrikerId(Long currentNonStrikerId) { this.currentNonStrikerId = currentNonStrikerId; }
    public Long getCurrentBowlerId() { return currentBowlerId; }
    public void setCurrentBowlerId(Long currentBowlerId) { this.currentBowlerId = currentBowlerId; }
    public int getPartnershipRuns() { return partnershipRuns; }
    public void setPartnershipRuns(int partnershipRuns) { this.partnershipRuns = partnershipRuns; }
    public int getPartnershipBalls() { return partnershipBalls; }
    public void setPartnershipBalls(int partnershipBalls) { this.partnershipBalls = partnershipBalls; }
    public List<BatsmanStats> getBatsmanStatsList() { return batsmanStatsList; }
    public void setBatsmanStatsList(List<BatsmanStats> batsmanStatsList) { this.batsmanStatsList = batsmanStatsList; }
    public List<BowlerStats> getBowlerStatsList() { return bowlerStatsList; }
    public void setBowlerStatsList(List<BowlerStats> bowlerStatsList) { this.bowlerStatsList = bowlerStatsList; }
    public List<FallOfWicket> getFallOfWicketsList() { return fallOfWicketsList; }
    public void setFallOfWicketsList(List<FallOfWicket> fallOfWicketsList) { this.fallOfWicketsList = fallOfWicketsList; }
    public List<String> getLastOverBalls() { return lastOverBalls; }
    public void setLastOverBalls(List<String> lastOverBalls) { this.lastOverBalls = lastOverBalls; }
    public List<String> getRecentBalls() { return recentBalls; }
    public void setRecentBalls(List<String> recentBalls) { this.recentBalls = recentBalls; }
}