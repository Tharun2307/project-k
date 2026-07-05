package com.mits.service.impl.score;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
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

        String sport = match.getSport().getSportName().toUpperCase();

        switch (sport) {

            case "CRICKET":
                updateCricketScore(event);
                break;

            case "VOLLEYBALL":
                updateVolleyballScore(event);
                break;

            case "KABADDI":
                updateKabaddiScore(event);
                break;

            case "BADMINTON":
                updateBadmintonScore(event);
                break;

            default:
                break;
        }
    }

    // ---------------- Cricket ----------------

    private void updateCricketScore(MatchEvent event) {

        Optional<CricketScore> optionalScore =
                cricketScoreRepository.findByMatch(event.getMatch());

        if (optionalScore.isEmpty())
            return;

        CricketScore score = optionalScore.get();

        switch (event.getEventType()) {

            case RUN:
                score.setRuns(score.getRuns() + 1);
                break;

            case FOUR:
                score.setRuns(score.getRuns() + 4);
                break;

            case SIX:
                score.setRuns(score.getRuns() + 6);
                break;

            case WICKET:
                score.setWickets(score.getWickets() + 1);
                break;

            default:
                return;
        }

        score.setOvers(event.getEventTime());

        cricketScoreRepository.save(score);
    }

    // ---------------- Volleyball ----------------

    private void updateVolleyballScore(MatchEvent event) {

        Optional<VolleyballScore> optionalScore =
                volleyballScoreRepository.findByMatch(event.getMatch());

        if (optionalScore.isEmpty())
            return;

        VolleyballScore score = optionalScore.get();

        switch (event.getEventType()) {

            case POINT:
                score.setTeam1Points(score.getTeam1Points() + 1);
                break;

            case ACE:
                score.setTeam1Points(score.getTeam1Points() + 1);
                break;

            case SPIKE:
                score.setTeam1Points(score.getTeam1Points() + 1);
                break;

            case BLOCK:
                score.setTeam1Points(score.getTeam1Points() + 1);
                break;

            default:
                return;
        }

        volleyballScoreRepository.save(score);
    }

    // ---------------- Kabaddi ----------------

    private void updateKabaddiScore(MatchEvent event) {

        Optional<KabaddiScore> optionalScore =
                kabaddiScoreRepository.findByMatch(event.getMatch());

        if (optionalScore.isEmpty())
            return;

        KabaddiScore score = optionalScore.get();

        switch (event.getEventType()) {

            case RAID_POINT:
                score.setTeam1Points(score.getTeam1Points() + 1);
                break;

            case BONUS_POINT:
                score.setTeam1BonusPoints(score.getTeam1BonusPoints() + 1);
                score.setTeam1Points(score.getTeam1Points() + 1);
                break;

            case SUPER_TACKLE:
                score.setTeam1SuperTackles(score.getTeam1SuperTackles() + 1);
                score.setTeam1Points(score.getTeam1Points() + 2);
                break;

            default:
                return;
        }

        kabaddiScoreRepository.save(score);
    }

    // ---------------- Badminton ----------------

    private void updateBadmintonScore(MatchEvent event) {

        Optional<BadmintonScore> optionalScore =
                badmintonScoreRepository.findByMatch(event.getMatch());

        if (optionalScore.isEmpty())
            return;

        BadmintonScore score = optionalScore.get();

        if (event.getEventType() == EventType.POINT) {
            score.setPlayer1Points(score.getPlayer1Points() + 1);
        } else {
            return;
        }

        badmintonScoreRepository.save(score);
    }
}