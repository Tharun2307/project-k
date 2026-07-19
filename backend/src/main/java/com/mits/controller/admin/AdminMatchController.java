package com.mits.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Match;
import com.mits.entity.Sport;
import com.mits.entity.Team;
import com.mits.entity.score.BadmintonScore;
import com.mits.entity.score.CricketScore;
import com.mits.entity.score.KabaddiScore;
import com.mits.entity.score.VolleyballScore;
import com.mits.enums.MatchStatus;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.SportRepository;
import com.mits.repository.TeamRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.MatchService;
import com.mits.dto.MatchRequestDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/matches") 
public class AdminMatchController {

    private final MatchService matchService;
    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;
    
    // ✅ ADDED: Score Repositories to initialize blank scores
    private final CricketScoreRepository cricketScoreRepository;
    private final VolleyballScoreRepository volleyballScoreRepository;
    private final KabaddiScoreRepository kabaddiScoreRepository;
    private final BadmintonScoreRepository badmintonScoreRepository;

    public AdminMatchController(MatchService matchService, 
            SportRepository sportRepository, 
            TeamRepository teamRepository,
            CricketScoreRepository cricketScoreRepository,
            VolleyballScoreRepository volleyballScoreRepository,
            KabaddiScoreRepository kabaddiScoreRepository,
            BadmintonScoreRepository badmintonScoreRepository) {
        this.matchService = matchService;
        this.sportRepository = sportRepository;
        this.teamRepository = teamRepository;
        this.cricketScoreRepository = cricketScoreRepository;
        this.volleyballScoreRepository = volleyballScoreRepository;
        this.kabaddiScoreRepository = kabaddiScoreRepository;
        this.badmintonScoreRepository = badmintonScoreRepository;
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@Valid @RequestBody MatchRequestDTO dto) {
        Match match = buildMatchFromDto(dto);
        Match savedMatch = matchService.createMatch(match);
        
        // ✅ CRITICAL FIX: Automatically create the empty score record for this match
        initializeEmptyScore(savedMatch);
        
        return ResponseEntity.ok(savedMatch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long id, @Valid @RequestBody MatchRequestDTO dto) {
        Match existingMatch = matchService.getMatchById(id);
        
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", dto.getSportId()));
        Team team1 = teamRepository.findById(dto.getTeam1Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeam1Id()));
        Team team2 = teamRepository.findById(dto.getTeam2Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeam2Id()));

        existingMatch.setSport(sport);
        existingMatch.setTeam1(team1);
        existingMatch.setTeam2(team2);
        existingMatch.setMatchDate(dto.getMatchDate());
        
        if (dto.getStatus() != null) {
            existingMatch.setStatus(MatchStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        existingMatch.setCricketOvers(dto.getCricketOvers());

        Match updatedMatch = matchService.updateMatch(id, existingMatch);
        return ResponseEntity.ok(updatedMatch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.ok("Match deleted successfully.");
    }

    private Match buildMatchFromDto(MatchRequestDTO dto) {
        Sport sport = sportRepository.findById(dto.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", "id", dto.getSportId()));
        Team team1 = teamRepository.findById(dto.getTeam1Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeam1Id()));
        Team team2 = teamRepository.findById(dto.getTeam2Id())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeam2Id()));

        Match match = new Match();
        match.setSport(sport);
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setMatchDate(dto.getMatchDate());

        if (dto.getStatus() != null) {
            match.setStatus(MatchStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        match.setCricketOvers(dto.getCricketOvers());
        return match;
    }

    // ✅ HELPER METHOD: Creates the 0-0 score record based on the sport
    private void initializeEmptyScore(Match match) {
        String sportName = match.getSport().getSportName().toUpperCase();
        switch (sportName) {
            case "CRICKET":
                CricketScore cScore = new CricketScore();
                cScore.setMatch(match);
                if (match.getCricketOvers() != null) {
                    cScore.setTotalOversInInnings(match.getCricketOvers());
                }
                cricketScoreRepository.save(cScore);
                break;
            case "VOLLEYBALL":
                VolleyballScore vScore = new VolleyballScore();
                vScore.setMatch(match);
                volleyballScoreRepository.save(vScore);
                break;
            case "KABADDI":
                KabaddiScore kScore = new KabaddiScore();
                kScore.setMatch(match);
                kabaddiScoreRepository.save(kScore);
                break;
            case "BADMINTON":
                BadmintonScore bScore = new BadmintonScore();
                bScore.setMatch(match);
                badmintonScoreRepository.save(bScore);
                break;
        }
    }
}