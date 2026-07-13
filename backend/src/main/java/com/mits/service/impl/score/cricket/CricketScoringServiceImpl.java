package com.mits.service.impl.score.cricket;

import org.springframework.stereotype.Service;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.CricketScore;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.service.score.cricket.CricketScoringService;

@Service
public class CricketScoringServiceImpl implements CricketScoringService {

    private final CricketScoreRepository cricketScoreRepository;

    // Constructor Injection
    public CricketScoringServiceImpl(CricketScoreRepository cricketScoreRepository) {
        this.cricketScoreRepository = cricketScoreRepository;
    }

    @Override
    public void processCricketEvent(MatchEvent event, CricketScore score) {
        String eventType = event.getEventType().toUpperCase();
        int battingTeam = score.getBattingTeam();
        boolean isTeam1Batting = (battingTeam == 1);

        // 1. Handle the specific event (Runs, Wickets, Extras)
        switch (eventType) {
            case "RUN": 
            case "SINGLE":
                addRuns(score, 1, isTeam1Batting); 
                advanceBall(score, isTeam1Batting); 
                break;
            case "TWO": 
            case "DOUBLE":
                addRuns(score, 2, isTeam1Batting); 
                advanceBall(score, isTeam1Batting); 
                break;
            case "THREE": 
            case "TRIPLE":
                addRuns(score, 3, isTeam1Batting); 
                advanceBall(score, isTeam1Batting); 
                break;
            case "FOUR": 
            case "BOUNDARY":
                addRuns(score, 4, isTeam1Batting); 
                advanceBall(score, isTeam1Batting); 
                break;
            case "SIX": 
            case "MAXIMUM":
                addRuns(score, 6, isTeam1Batting); 
                advanceBall(score, isTeam1Batting); 
                break;
            
            case "WICKET": 
            case "OUT":
                addWicket(score, isTeam1Batting); 
                advanceBall(score, isTeam1Batting); 
                break;
            
            // Extras that don't count as a legal ball
            case "WIDE": 
                addRuns(score, 1, isTeam1Batting); 
                addExtra(score, "WIDE", isTeam1Batting); 
                break; 
            case "NO_BALL": 
            case "NOBALL":
                addRuns(score, 1, isTeam1Batting); 
                addExtra(score, "NO_BALL", isTeam1Batting); 
                break; 
            
            // Extras that count as a legal ball
            case "BYE": 
                addRuns(score, 1, isTeam1Batting); 
                addExtra(score, "BYE", isTeam1Batting); 
                advanceBall(score, isTeam1Batting); 
                break;
            case "LEG_BYE": 
            case "LEGBYE":
                addRuns(score, 1, isTeam1Batting); 
                addExtra(score, "LEG_BYE", isTeam1Batting); 
                advanceBall(score, isTeam1Batting); 
                break;
                
            default:
                // If it's an unknown event type (like a commentary-only event), just save and return
                cricketScoreRepository.save(score);
                return;
        }

        // 2. Check if Innings is Over (10 wickets OR max overs reached)
        boolean inningsOver = false;
        if (isTeam1Batting) {
            if (score.getTeam1Wickets() == 10 || score.getTeam1Overs() >= score.getTotalOversInInnings()) {
                inningsOver = true;
            }
        } else {
            if (score.getTeam2Wickets() == 10 || score.getTeam2Overs() >= score.getTotalOversInInnings()) {
                inningsOver = true;
            }
        }

        // 3. Handle Innings Transition or Calculate Run Rates
        if (inningsOver) {
            handleInningsEnd(score);
        } else {
            calculateRunRates(score);
        }

        // 4. Save the updated score to the database
        cricketScoreRepository.save(score);
    }

    // --- PRIVATE HELPER METHODS ---

    private void addRuns(CricketScore score, int runs, boolean isTeam1) {
        if (isTeam1) score.setTeam1Runs(score.getTeam1Runs() + runs);
        else score.setTeam2Runs(score.getTeam2Runs() + runs);
    }

    private void addWicket(CricketScore score, boolean isTeam1) {
        if (isTeam1) score.setTeam1Wickets(score.getTeam1Wickets() + 1);
        else score.setTeam2Wickets(score.getTeam2Wickets() + 1);
    }

    private void addExtra(CricketScore score, String type, boolean isTeam1) {
        if (isTeam1) {
            score.setTeam1Extras(score.getTeam1Extras() + 1);
            if ("WIDE".equals(type)) score.setTeam1Wides(score.getTeam1Wides() + 1);
            if ("NO_BALL".equals(type)) score.setTeam1NoBalls(score.getTeam1NoBalls() + 1);
            if ("BYE".equals(type)) score.setTeam1Byes(score.getTeam1Byes() + 1);
            if ("LEG_BYE".equals(type)) score.setTeam1LegByes(score.getTeam1LegByes() + 1);
        } else {
            score.setTeam2Extras(score.getTeam2Extras() + 1);
            if ("WIDE".equals(type)) score.setTeam2Wides(score.getTeam2Wides() + 1);
            if ("NO_BALL".equals(type)) score.setTeam2NoBalls(score.getTeam2NoBalls() + 1);
            if ("BYE".equals(type)) score.setTeam2Byes(score.getTeam2Byes() + 1);
            if ("LEG_BYE".equals(type)) score.setTeam2LegByes(score.getTeam2LegByes() + 1);
        }
    }

    private void advanceBall(CricketScore score, boolean isTeam1) {
        if (isTeam1) {
            score.setTeam1Balls(score.getTeam1Balls() + 1);
            if (score.getTeam1Balls() == 6) {
                score.setTeam1Overs(score.getTeam1Overs() + 1);
                score.setTeam1Balls(0);
            }
        } else {
            score.setTeam2Balls(score.getTeam2Balls() + 1);
            if (score.getTeam2Balls() == 6) {
                score.setTeam2Overs(score.getTeam2Overs() + 1);
                score.setTeam2Balls(0);
            }
        }
    }

    private void handleInningsEnd(CricketScore score) {
        if (score.getCurrentInnings() == 1) {
            // End of 1st Innings. Set target for 2nd innings.
            int firstInningsScore = (score.getBattingTeam() == 1) ? score.getTeam1Runs() : score.getTeam2Runs();
            score.setTarget(firstInningsScore + 1);
            
            // Switch to 2nd Innings
            score.setCurrentInnings(2);
            score.setBattingTeam(score.getBattingTeam() == 1 ? 2 : 1); // Toggle batting team
            
            // Reset run rates for the new innings
            score.setCurrentRunRate(0.0);
            score.setRequiredRunRate(0.0);
        } else {
            // End of 2nd Innings. Match Over. Calculate Result.
            int team1Score = score.getTeam1Runs();
            int team2Score = score.getTeam2Runs();
            
            if (team1Score > team2Score) {
                int runsMargin = team1Score - team2Score;
                score.setResult("Team 1 won by " + runsMargin + " runs");
            } else if (team2Score > team1Score) {
                int wicketsRemaining = 10 - score.getTeam2Wickets(); 
                score.setResult("Team 2 won by " + wicketsRemaining + " wickets");
            } else {
                score.setResult("Match Tied");
            }
        }
    }

    private void calculateRunRates(CricketScore score) {
        int battingTeam = score.getBattingTeam();
        int runs = (battingTeam == 1) ? score.getTeam1Runs() : score.getTeam2Runs();
        int overs = (battingTeam == 1) ? score.getTeam1Overs() : score.getTeam2Overs();
        int balls = (battingTeam == 1) ? score.getTeam1Balls() : score.getTeam2Balls();

        // Calculate total overs as a decimal (e.g., 15 overs and 3 balls = 15.5 overs)
        double totalOversBowled = overs + (balls / 6.0);
        
        // Current Run Rate
        if (totalOversBowled > 0) {
            double crr = runs / totalOversBowled;
            score.setCurrentRunRate(Math.round(crr * 100.0) / 100.0); // Round to 2 decimal places
        }

        // Required Run Rate (Only in 2nd Innings)
        if (score.getCurrentInnings() == 2 && score.getTarget() > 0) {
            int runsNeeded = score.getTarget() - runs;
            double remainingOvers = score.getTotalOversInInnings() - totalOversBowled;
            
            if (remainingOvers > 0) {
                double rrr = runsNeeded / remainingOvers;
                score.setRequiredRunRate(Math.round(rrr * 100.0) / 100.0);
            } else {
                score.setRequiredRunRate(99.99); // Mathematically impossible
            }
        }
    }
}