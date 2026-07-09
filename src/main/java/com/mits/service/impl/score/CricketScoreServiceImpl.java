package com.mits.service.impl.score;

import java.util.List;
import org.springframework.stereotype.Service;

import com.mits.dto.score.CricketScoreRequestDTO;
import com.mits.entity.Match;
import com.mits.entity.score.CricketScore;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.MatchRepository;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.service.score.CricketScoreService;

@Service
public class CricketScoreServiceImpl implements CricketScoreService {

    private final CricketScoreRepository repository;
    private final MatchRepository matchRepository; // Added to fetch Match by ID

    public CricketScoreServiceImpl(CricketScoreRepository repository, MatchRepository matchRepository) {
        this.repository = repository;
        this.matchRepository = matchRepository;
    }

    @Override
    public CricketScore createScore(CricketScoreRequestDTO dto) {
        // Fetch the actual Match entity from the database
        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        CricketScore score = new CricketScore();
        score.setMatch(match);
        mapDtoToEntity(dto, score);
        
        return repository.save(score);
    }

    @Override
    public CricketScore getScoreById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CricketScore", "id", id));
    }

    @Override
    public List<CricketScore> getAllScores() {
        return repository.findAll();
    }

    @Override
    public CricketScore updateScore(Long id, CricketScoreRequestDTO dto) {
        CricketScore existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CricketScore", "id", id));

        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        existing.setMatch(match);
        mapDtoToEntity(dto, existing);

        return repository.save(existing);
    }

    @Override
    public void deleteScore(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("CricketScore", "id", id);
        }
        repository.deleteById(id);
    }

    // Helper method to map DTO fields to Entity fields
    private void mapDtoToEntity(CricketScoreRequestDTO dto, CricketScore score) {
        score.setTeam1Runs(dto.getTeam1Runs());
        score.setTeam1Wickets(dto.getTeam1Wickets());
        score.setTeam2Runs(dto.getTeam2Runs());
        score.setTeam2Wickets(dto.getTeam2Wickets());
        score.setOvers(dto.getOvers());
        score.setExtras(dto.getExtras());
        score.setTarget(dto.getTarget());
        score.setInnings(dto.getInnings());
    }
}