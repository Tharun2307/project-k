package com.mits.service.impl.score.cricket;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.score.*;
import com.mits.entity.score.cricket.BatsmanStats;
import com.mits.entity.score.cricket.BowlerStats;
import com.mits.entity.score.cricket.FallOfWicket;
import com.mits.enums.DismissalType;
import com.mits.enums.MatchState;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.service.score.cricket.CricketScoringService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CricketScoringServiceImpl implements CricketScoringService {

    private final CricketScoreRepository cricketScoreRepository;
    private final MatchEventRepository matchEventRepository;

    public CricketScoringServiceImpl(CricketScoreRepository cricketScoreRepository, MatchEventRepository matchEventRepository) {
        this.cricketScoreRepository = cricketScoreRepository;
        this.matchEventRepository = matchEventRepository;
    }

    @Override
    public void processCricketEvent(MatchEvent event, CricketScore score) {
        processEventInternal(score, event);
        calculateRunRates(score);
        cricketScoreRepository.save(score);
    }

    @Override
    public void recalculateScore(Match match) {
        CricketScore score = cricketScoreRepository.findByMatch(match).orElse(new CricketScore());
        resetScore(score);
        
        List<MatchEvent> events = matchEventRepository.findByMatchOrderByTimestampAsc(match);
        for (MatchEvent event : events) {
            processEventInternal(score, event);
        }
        calculateRunRates(score);
        cricketScoreRepository.save(score);
    }

    private void resetScore(CricketScore score) {
        score.setTeam1Runs(0); score.setTeam1Wickets(0); score.setTeam1Overs(0); score.setTeam1Balls(0);
        score.setTeam1Extras(0); score.setTeam1Wides(0); score.setTeam1NoBalls(0); score.setTeam1Byes(0); score.setTeam1LegByes(0);
        score.setTeam2Runs(0); score.setTeam2Wickets(0); score.setTeam2Overs(0); score.setTeam2Balls(0);
        score.setTeam2Extras(0); score.setTeam2Wides(0); score.setTeam2NoBalls(0); score.setTeam2Byes(0); score.setTeam2LegByes(0);
        score.setTotalLegalBallsTeam1(0); score.setTotalLegalBallsTeam2(0);
        score.setTarget(0); score.setCurrentRunRate(0.0); score.setRequiredRunRate(0.0);
        score.setResult(null); score.setMatchState(MatchState.FIRST_INNINGS);
        score.setCurrentInnings(1); score.setBattingTeam(1);
        score.setPartnershipRuns(0); score.setPartnershipBalls(0);
        score.getBatsmanStatsList().clear();
        score.getBowlerStatsList().clear();
        score.getFallOfWicketsList().clear();
        score.getLastOverBalls().clear();
        score.getRecentBalls().clear();
    }

    private void processEventInternal(CricketScore score, MatchEvent event) {
        validateEvent(score, event);
        
        boolean isTeam1 = score.getBattingTeam() == 1;
        String eventType = event.getEventType().toUpperCase();
        int runs = event.getRuns() != null ? event.getRuns() : 0;
        
        boolean isLegalBall = true;
        boolean isWicket = false;
        String ballSymbol = String.valueOf(runs);

        if (eventType.equals("WIDE") || eventType.equals("NO_BALL")) {
            isLegalBall = false;
            ballSymbol = eventType.equals("WIDE") ? "Wd" : "Nb";
            if (runs > 1) ballSymbol += "+" + (runs - (eventType.equals("NO_BALL") ? 1 : 0));
        } else if (eventType.equals("BYE") || eventType.equals("LEG_BYE")) {
            ballSymbol = eventType.equals("BYE") ? "B" : "Lb";
            if (runs > 1) ballSymbol += runs;
        } else if (eventType.equals("DOT")) {
            ballSymbol = ".";
            runs = 0;
        } else if (eventType.equals("FOUR")) {
            ballSymbol = "4"; runs = 4;
        } else if (eventType.equals("SIX")) {
            ballSymbol = "6"; runs = 6;
        } else if (eventType.matches("BOWLED|CAUGHT|LBW|RUN_OUT|HIT_WICKET|STUMPED")) {
            isWicket = true;
            ballSymbol = "W";
        } else if (eventType.equals("SINGLE")) {
            runs = 1; ballSymbol = "1";
        } else if (eventType.equals("DOUBLE")) {
            runs = 2; ballSymbol = "2";
        } else if (eventType.equals("TRIPLE")) {
            runs = 3; ballSymbol = "3";
        }

        int battingRuns = runs;
        int extraRuns = 0;
        if (eventType.equals("WIDE")) {
            extraRuns = runs; battingRuns = 0;
        } else if (eventType.equals("NO_BALL")) {
            extraRuns = 1; battingRuns = Math.max(0, runs - 1);
        } else if (eventType.equals("BYE") || eventType.equals("LEG_BYE")) {
            extraRuns = runs; battingRuns = 0;
        }

        updateTeamScore(score, isTeam1, runs, extraRuns, eventType, isWicket);
        
        if (isLegalBall) {
            if (isTeam1) score.setTotalLegalBallsTeam1(score.getTotalLegalBallsTeam1() + 1);
            else score.setTotalLegalBallsTeam2(score.getTotalLegalBallsTeam2() + 1);
        }

        if (isLegalBall || eventType.equals("BYE") || eventType.equals("LEG_BYE")) {
            updateBatsman(score, event, battingRuns, isWicket);
        }
        updateBowler(score, event, runs, isWicket, isLegalBall);

        if (!isWicket) {
            score.setPartnershipRuns(score.getPartnershipRuns() + battingRuns);
            if (isLegalBall) score.setPartnershipBalls(score.getPartnershipBalls() + 1);
        } else {
            recordFallOfWicket(score, event);
            score.setPartnershipRuns(0);
            score.setPartnershipBalls(0);
        }

        if (isLegalBall && (battingRuns == 1 || battingRuns == 3)) {
            rotateStrike(score);
        }

        updateBallHistory(score, ballSymbol, isLegalBall);

        int currentLegalBalls = isTeam1 ? score.getTotalLegalBallsTeam1() : score.getTotalLegalBallsTeam2();
        if (isLegalBall && currentLegalBalls % 6 == 0) {
            rotateStrike(score);
            score.setLastOverBalls(new ArrayList<>());
        }

        checkChaseCompletion(score, isTeam1);
        checkInningsEnd(score, isTeam1, currentLegalBalls);
    }

    private void validateEvent(CricketScore score, MatchEvent event) {
        if (score.getMatchState() == MatchState.COMPLETED) {
            throw new IllegalStateException("Match is already completed.");
        }
        if (event.getRuns() != null && event.getRuns() < 0) {
            throw new IllegalArgumentException("Runs cannot be negative.");
        }
    }

    private void updateTeamScore(CricketScore score, boolean isTeam1, int totalRuns, int extraRuns, String eventType, boolean isWicket) {
        if (isTeam1) {
            score.setTeam1Runs(score.getTeam1Runs() + totalRuns);
            score.setTeam1Extras(score.getTeam1Extras() + extraRuns);
            if (eventType.equals("WIDE")) score.setTeam1Wides(score.getTeam1Wides() + extraRuns);
            if (eventType.equals("NO_BALL")) score.setTeam1NoBalls(score.getTeam1NoBalls() + extraRuns);
            if (eventType.equals("BYE")) score.setTeam1Byes(score.getTeam1Byes() + extraRuns);
            if (eventType.equals("LEG_BYE")) score.setTeam1LegByes(score.getTeam1LegByes() + extraRuns);
            if (isWicket) score.setTeam1Wickets(score.getTeam1Wickets() + 1);
        } else {
            score.setTeam2Runs(score.getTeam2Runs() + totalRuns);
            score.setTeam2Extras(score.getTeam2Extras() + extraRuns);
            if (eventType.equals("WIDE")) score.setTeam2Wides(score.getTeam2Wides() + extraRuns);
            if (eventType.equals("NO_BALL")) score.setTeam2NoBalls(score.getTeam2NoBalls() + extraRuns);
            if (eventType.equals("BYE")) score.setTeam2Byes(score.getTeam2Byes() + extraRuns);
            if (eventType.equals("LEG_BYE")) score.setTeam2LegByes(score.getTeam2LegByes() + extraRuns);
            if (isWicket) score.setTeam2Wickets(score.getTeam2Wickets() + 1);
        }
    }

    private void updateBatsman(CricketScore score, MatchEvent event, int runs, boolean isWicket) {
        Long strikerId = score.getCurrentStrikerId() != null ? score.getCurrentStrikerId() : (event.getPlayer() != null ? event.getPlayer().getId() : 0L);
        score.setCurrentStrikerId(strikerId);

        BatsmanStats stats = score.getBatsmanStatsList().stream()
                .filter(b -> b.getPlayerId().equals(strikerId))
                .findFirst().orElseGet(() -> {
                    BatsmanStats newStats = new BatsmanStats();
                    newStats.setPlayerId(strikerId);
                    newStats.setPlayerName(event.getPlayer() != null ? event.getPlayer().getPlayerName() : "Unknown");
                    score.getBatsmanStatsList().add(newStats);
                    return newStats;
                });

        stats.setRuns(stats.getRuns() + runs);
        stats.setBalls(stats.getBalls() + 1);
        if (runs == 4) stats.setFours(stats.getFours() + 1);
        if (runs == 6) stats.setSixes(stats.getSixes() + 1);
        
        if (isWicket) {
            stats.setOut(true);
            stats.setDismissalType(DismissalType.valueOf(event.getEventType().toUpperCase()));
        }
        stats.setStrikeRate(stats.getBalls() > 0 ? Math.round((stats.getRuns() * 100.0 / stats.getBalls()) * 100.0) / 100.0 : 0.0);
    }

    private void updateBowler(CricketScore score, MatchEvent event, int runsGiven, boolean isWicket, boolean isLegalBall) {
        Long bowlerId = score.getCurrentBowlerId() != null ? score.getCurrentBowlerId() : (event.getPlayer() != null ? event.getPlayer().getId() : 0L);
        score.setCurrentBowlerId(bowlerId);

        BowlerStats stats = score.getBowlerStatsList().stream()
                .filter(b -> b.getPlayerId().equals(bowlerId))
                .findFirst().orElseGet(() -> {
                    BowlerStats newStats = new BowlerStats();
                    newStats.setPlayerId(bowlerId);
                    newStats.setPlayerName(event.getPlayer() != null ? event.getPlayer().getPlayerName() : "Unknown");
                    score.getBowlerStatsList().add(newStats);
                    return newStats;
                });

        stats.setRunsGiven(stats.getRunsGiven() + runsGiven);
        if (isWicket) stats.setWickets(stats.getWickets() + 1);
        
        if (isLegalBall) {
            stats.setBalls(stats.getBalls() + 1);
            if (stats.getBalls() == 6) {
                stats.setOvers(stats.getOvers() + 1);
                stats.setBalls(0);
            }
        }
        int totalBallsBowled = (stats.getOvers() * 6) + stats.getBalls();
        stats.setEconomy(totalBallsBowled > 0 ? Math.round((stats.getRunsGiven() * 6.0 / totalBallsBowled) * 100.0) / 100.0 : 0.0);
    }

    private void rotateStrike(CricketScore score) {
        Long temp = score.getCurrentStrikerId();
        score.setCurrentStrikerId(score.getCurrentNonStrikerId());
        score.setCurrentNonStrikerId(temp);
    }

    private void recordFallOfWicket(CricketScore score, MatchEvent event) {
        boolean isTeam1 = score.getBattingTeam() == 1;
        int currentScore = isTeam1 ? score.getTeam1Runs() : score.getTeam2Runs();
        int legalBalls = isTeam1 ? score.getTotalLegalBallsTeam1() : score.getTotalLegalBallsTeam2();
        int overs = legalBalls / 6;
        int balls = legalBalls % 6;
        
        FallOfWicket fow = new FallOfWicket();
        fow.setScore(currentScore);
        fow.setOvers(overs + "." + balls);
        fow.setBatsmanName(event.getPlayer() != null ? event.getPlayer().getPlayerName() : "Unknown");
        score.getFallOfWicketsList().add(fow);
    }

    private void updateBallHistory(CricketScore score, String ballSymbol, boolean isLegalBall) {
        score.getLastOverBalls().add(ballSymbol);
        score.getRecentBalls().add(ballSymbol);
        if (score.getRecentBalls().size() > 20) {
            score.getRecentBalls().remove(0);
        }
    }

    private void checkChaseCompletion(CricketScore score, boolean isTeam1) {
        if (score.getCurrentInnings() == 2) {
            int battingRuns = isTeam1 ? score.getTeam1Runs() : score.getTeam2Runs();
            if (battingRuns >= score.getTarget()) {
                int wicketsLost = isTeam1 ? score.getTeam1Wickets() : score.getTeam2Wickets();
                int legalBalls = isTeam1 ? score.getTotalLegalBallsTeam1() : score.getTotalLegalBallsTeam2();
                int maxBalls = score.getTotalOversInInnings() * 6;
                int ballsRemaining = maxBalls - legalBalls;
                finishMatch(score, "Team " + score.getBattingTeam() + " won by " + (10 - wicketsLost) + " wickets with " + ballsRemaining + " balls remaining");
            }
        }
    }

    private void checkInningsEnd(CricketScore score, boolean isTeam1, int currentLegalBalls) {
        int wickets = isTeam1 ? score.getTeam1Wickets() : score.getTeam2Wickets();
        int maxBalls = score.getTotalOversInInnings() * 6;

        if (wickets >= 10 || currentLegalBalls >= maxBalls) {
            if (score.getCurrentInnings() == 1) {
                int firstInningsScore = score.getBattingTeam() == 1 ? score.getTeam1Runs() : score.getTeam2Runs();
                score.setTarget(firstInningsScore + 1);
                score.setCurrentInnings(2);
                score.setBattingTeam(score.getBattingTeam() == 1 ? 2 : 1);
                score.setMatchState(MatchState.SECOND_INNINGS);
                score.setPartnershipRuns(0);
                score.setPartnershipBalls(0);
                score.setLastOverBalls(new ArrayList<>());
            } else {
                int team1Score = score.getTeam1Runs();
                int team2Score = score.getTeam2Runs();
                if (team1Score > team2Score) {
                    finishMatch(score, "Team 1 won by " + (team1Score - team2Score) + " runs");
                } else if (team2Score > team1Score) {
                    finishMatch(score, "Team 2 won by " + (10 - score.getTeam2Wickets()) + " wickets");
                } else {
                    finishMatch(score, "Match Tied");
                }
            }
        }
    }

    private void finishMatch(CricketScore score, String result) {
        score.setResult(result);
        score.setMatchState(MatchState.COMPLETED);
        score.setRequiredRunRate(0.0);
    }

    private void calculateRunRates(CricketScore score) {
        boolean isTeam1 = score.getBattingTeam() == 1;
        int runs = isTeam1 ? score.getTeam1Runs() : score.getTeam2Runs();
        int legalBalls = isTeam1 ? score.getTotalLegalBallsTeam1() : score.getTotalLegalBallsTeam2();
        double totalOversBowled = legalBalls / 6.0;
        
        if (totalOversBowled > 0) {
            score.setCurrentRunRate(Math.round((runs / totalOversBowled) * 100.0) / 100.0);
        }

        if (score.getCurrentInnings() == 2 && score.getTarget() > 0) {
            int runsNeeded = score.getTarget() - runs;
            int maxBalls = score.getTotalOversInInnings() * 6;
            double remainingOvers = (maxBalls - legalBalls) / 6.0;
            
            if (remainingOvers > 0 && runsNeeded > 0) {
                score.setRequiredRunRate(Math.round((runsNeeded / remainingOvers) * 100.0) / 100.0);
            } else {
                score.setRequiredRunRate(0.0);
            }
        }
    }
}