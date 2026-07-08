package com.mits.service.impl.score;

import com.mits.dto.LiveScoreResponse;
import com.mits.entity.Match;
import com.mits.entity.score.BadmintonScore;
import com.mits.entity.score.CricketScore;
import com.mits.entity.score.KabaddiScore;
import com.mits.entity.score.VolleyballScore;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.MatchRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.score.LiveScoreService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LiveScoreServiceImpl implements LiveScoreService {

    private final MatchRepository matchRepository;
    private final CricketScoreRepository cricketScoreRepository;
    private final VolleyballScoreRepository volleyballScoreRepository;
    private final KabaddiScoreRepository kabaddiScoreRepository;
    private final BadmintonScoreRepository badmintonScoreRepository;

    public LiveScoreServiceImpl(MatchRepository matchRepository,
                                CricketScoreRepository cricketScoreRepository,
                                VolleyballScoreRepository volleyballScoreRepository,
                                KabaddiScoreRepository kabaddiScoreRepository,
                                BadmintonScoreRepository badmintonScoreRepository) {
        this.matchRepository = matchRepository;
        this.cricketScoreRepository = cricketScoreRepository;
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.kabaddiScoreRepository = kabaddiScoreRepository;
        this.badmintonScoreRepository = badmintonScoreRepository;
    }

    @Override
    public LiveScoreResponse getLiveScoreByMatchId(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", matchId));

        LiveScoreResponse response = new LiveScoreResponse();
        response.setMatchId(match.getId());
        response.setSportName(match.getSport().getSportName());
        response.setTeam1Name(match.getTeam1().getTeamName());
        response.setTeam2Name(match.getTeam2().getTeamName());
        response.setStatus(match.getStatus().name()); // Converts Enum to String

        Map<String, Object> scoreDetails = new HashMap<>();
        String sport = match.getSport().getSportName().toUpperCase();

        switch (sport) {
            case "CRICKET":
                Optional<CricketScore> cScore = cricketScoreRepository.findByMatch(match);
                if (cScore.isPresent()) {
                    CricketScore cs = cScore.get();
                    scoreDetails.put("team1Runs", cs.getTeam1Runs());
                    scoreDetails.put("team1Wickets", cs.getTeam1Wickets());
                    scoreDetails.put("team2Runs", cs.getTeam2Runs());
                    scoreDetails.put("team2Wickets", cs.getTeam2Wickets());
                    scoreDetails.put("overs", cs.getOvers());
                }
                break;

            case "VOLLEYBALL":
                Optional<VolleyballScore> vScore = volleyballScoreRepository.findByMatch(match);
                if (vScore.isPresent()) {
                    VolleyballScore vs = vScore.get();
                    scoreDetails.put("team1Points", vs.getTeam1Points());
                    scoreDetails.put("team2Points", vs.getTeam2Points());
                    scoreDetails.put("team1SetsWon", vs.getTeam1SetsWon());
                    scoreDetails.put("team2SetsWon", vs.getTeam2SetsWon());
                    scoreDetails.put("currentSet", vs.getCurrentSet());
                }
                break;

            case "KABADDI":
                Optional<KabaddiScore> kScore = kabaddiScoreRepository.findByMatch(match);
                if (kScore.isPresent()) {
                    KabaddiScore ks = kScore.get();
                    scoreDetails.put("team1Points", ks.getTeam1Points());
                    scoreDetails.put("team2Points", ks.getTeam2Points());
                    scoreDetails.put("team1Bonus", ks.getTeam1BonusPoints());
                    scoreDetails.put("team2Bonus", ks.getTeam2BonusPoints());
                    scoreDetails.put("team1SuperTackles", ks.getTeam1SuperTackles());
                    scoreDetails.put("team2SuperTackles", ks.getTeam2SuperTackles());
                }
                break;

            case "BADMINTON":
                Optional<BadmintonScore> bScore = badmintonScoreRepository.findByMatch(match);
                if (bScore.isPresent()) {
                    BadmintonScore bs = bScore.get();
                    scoreDetails.put("player1Points", bs.getPlayer1Points());
                    scoreDetails.put("player2Points", bs.getPlayer2Points());
                    scoreDetails.put("player1SetsWon", bs.getPlayer1SetsWon());
                    scoreDetails.put("player2SetsWon", bs.getPlayer2SetsWon());
                }
                break;
        }

        response.setScoreDetails(scoreDetails);
        return response;
    }
}