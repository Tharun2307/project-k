package com.mits.service.impl.score;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.Team; // Added import for Team
import com.mits.entity.score.BadmintonScore;
import com.mits.entity.score.CricketScore;
import com.mits.entity.score.KabaddiScore;
import com.mits.entity.score.VolleyballScore;
import com.mits.enums.EventType;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.score.ScoreUpdateService;

@Service
public class ScoreUpdateServiceImpl implements ScoreUpdateService {

    private final CricketScoreRepository cricketScoreRepository;
    private final VolleyballScoreRepository volleyballScoreRepository;
    private final KabaddiScoreRepository kabaddiScoreRepository;
    private final BadmintonScoreRepository badmintonScoreRepository;

    public ScoreUpdateServiceImpl(
            CricketScoreRepository cricketScoreRepository,
            VolleyballScoreRepository volleyballScoreRepository,
            KabaddiScoreRepository kabaddiScoreRepository,
            BadmintonScoreRepository badmintonScoreRepository) {

        this.cricketScoreRepository = cricketScoreRepository;
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.kabaddiScoreRepository = kabaddiScoreRepository;
        this.badmintonScoreRepository = badmintonScoreRepository;
    }

    @Override
    public void updateScore(MatchEvent event) {

        Match match = event.getMatch();

        // 1. Determine the scoring team (Fallback to player's team if event team is null)
        Team scoringTeam = event.getTeam();
        if (scoringTeam == null && event.getPlayer() != null) {
            scoringTeam = event.getPlayer().getTeam();
        }

        if (scoringTeam == null) {
            throw new IllegalArgumentException("Scoring team could not be determined from the event.");
        }

        // 2. Check if the scoring team is Team 1 or Team 2
        boolean isTeam1 = match.getTeam1().getId().equals(scoringTeam.getId());

        String sport = match.getSport().getSportName().toUpperCase();

        // 3. Pass the 'isTeam1' flag to the specific sport methods
        switch (sport) {

            case "CRICKET":
                updateCricketScore(event, isTeam1);
                break;

            case "VOLLEYBALL":
                updateVolleyballScore(event, isTeam1);
                break;

            case "KABADDI":
                updateKabaddiScore(event, isTeam1);
                break;

            case "BADMINTON":
                updateBadmintonScore(event, isTeam1);
                break;

            default:
                break;
        }
    }

    // ---------------- Cricket ----------------

    private void updateCricketScore(MatchEvent event, boolean isTeam1) {

        Optional<CricketScore> optionalScore =
                cricketScoreRepository.findByMatch(event.getMatch());

        if (optionalScore.isEmpty())
            return;

        CricketScore score = optionalScore.get();

        switch (event.getEventType()) {

            case RUN:
                if (isTeam1) score.setTeam1Runs(score.getTeam1Runs() + 1);
                else score.setTeam2Runs(score.getTeam2Runs() + 1);
                break;

            case FOUR:
                if (isTeam1) score.setTeam1Runs(score.getTeam1Runs() + 4);
                else score.setTeam2Runs(score.getTeam2Runs() + 4);
                break;

            case SIX:
                if (isTeam1) score.setTeam1Runs(score.getTeam1Runs() + 6);
                else score.setTeam2Runs(score.getTeam2Runs() + 6);
                break;

            case WICKET:
                if (isTeam1) score.setTeam1Wickets(score.getTeam1Wickets() + 1);
                else score.setTeam2Wickets(score.getTeam2Wickets() + 1);
                break;

            default:
                return;
        }

        // --- FIX APPLIED HERE ---
        // Convert the String from event.getEventTime() into a double
        try {
            score.setOvers(Double.parseDouble(event.getEventTime()));
        } catch (NumberFormatException e) {
            // If the string isn't a valid number (e.g. it's a timestamp), we just skip updating overs
            System.err.println("Warning: Could not parse event time to overs. Value was: " + event.getEventTime());
        }

        cricketScoreRepository.save(score);
    }

    // ---------------- Volleyball ----------------

    private void updateVolleyballScore(MatchEvent event, boolean isTeam1) {

        Optional<VolleyballScore> optionalScore =
                volleyballScoreRepository.findByMatch(event.getMatch());

        if (optionalScore.isEmpty())
            return;

        VolleyballScore score = optionalScore.get();

        switch (event.getEventType()) {

            case POINT:
            case ACE:
            case SPIKE:
            case BLOCK:
                if (isTeam1) score.setTeam1Points(score.getTeam1Points() + 1);
                else score.setTeam2Points(score.getTeam2Points() + 1);
                break;

            default:
                return;
        }

        volleyballScoreRepository.save(score);
    }

    // ---------------- Kabaddi ----------------

    private void updateKabaddiScore(MatchEvent event, boolean isTeam1) {

        Optional<KabaddiScore> optionalScore =
                kabaddiScoreRepository.findByMatch(event.getMatch());

        if (optionalScore.isEmpty())
            return;

        KabaddiScore score = optionalScore.get();

        switch (event.getEventType()) {

            case RAID_POINT:
                if (isTeam1) score.setTeam1Points(score.getTeam1Points() + 1);
                else score.setTeam2Points(score.getTeam2Points() + 1);
                break;

            case BONUS_POINT:
                if (isTeam1) {
                    score.setTeam1BonusPoints(score.getTeam1BonusPoints() + 1);
                    score.setTeam1Points(score.getTeam1Points() + 1);
                } else {
                    score.setTeam2BonusPoints(score.getTeam2BonusPoints() + 1);
                    score.setTeam2Points(score.getTeam2Points() + 1);
                }
                break;

            case SUPER_TACKLE:
                if (isTeam1) {
                    score.setTeam1SuperTackles(score.getTeam1SuperTackles() + 1);
                    score.setTeam1Points(score.getTeam1Points() + 2);
                } else {
                    score.setTeam2SuperTackles(score.getTeam2SuperTackles() + 1);
                    score.setTeam2Points(score.getTeam2Points() + 2);
                }
                break;

            default:
                return;
        }

        kabaddiScoreRepository.save(score);
    }

    // ---------------- Badminton ----------------

    private void updateBadmintonScore(MatchEvent event, boolean isTeam1) {

        Optional<BadmintonScore> optionalScore =
                badmintonScoreRepository.findByMatch(event.getMatch());

        if (optionalScore.isEmpty())
            return;

        BadmintonScore score = optionalScore.get();

        if (event.getEventType() == EventType.POINT) {
            if (isTeam1) score.setPlayer1Points(score.getPlayer1Points() + 1);
            else score.setPlayer2Points(score.getPlayer2Points() + 1);
        } else {
            return;
        }

        badmintonScoreRepository.save(score);
    }
}