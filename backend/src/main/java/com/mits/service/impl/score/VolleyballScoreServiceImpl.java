package com.mits.service.impl.score;

import java.util.List;
import org.springframework.stereotype.Service;

import com.mits.dto.score.VolleyballScoreRequestDTO;
import com.mits.entity.Match;
import com.mits.entity.score.VolleyballScore;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.MatchRepository;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.score.VolleyballScoreService;

@Service
public class VolleyballScoreServiceImpl implements VolleyballScoreService {

    private final VolleyballScoreRepository repository;
    private final MatchRepository matchRepository; // Added to fetch Match by ID

    public VolleyballScoreServiceImpl(VolleyballScoreRepository repository, MatchRepository matchRepository) {
        this.repository = repository;
        this.matchRepository = matchRepository;
    }

    @Override
    public VolleyballScore createScore(VolleyballScoreRequestDTO dto) {
        // Fetch the actual Match entity from the database
        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        VolleyballScore score = new VolleyballScore();
        score.setMatch(match);
        mapDtoToEntity(dto, score);
        
        return repository.save(score);
    }

    @Override
    public VolleyballScore getScoreById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VolleyballScore", "id", id));
    }

    @Override
    public List<VolleyballScore> getAllScores() {
        return repository.findAll();
    }

    @Override
    public VolleyballScore updateScore(Long id, VolleyballScoreRequestDTO dto) {
        VolleyballScore existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VolleyballScore", "id", id));

        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        existing.setMatch(match);
        mapDtoToEntity(dto, existing);

        return repository.save(existing);
    }

    @Override
    public void deleteScore(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("VolleyballScore", "id", id);
        }
        repository.deleteById(id);
    }

    // Helper method to map DTO fields to Entity fields
    private void mapDtoToEntity(VolleyballScoreRequestDTO dto, VolleyballScore score) {
        score.setTeam1SetsWon(dto.getTeam1SetsWon());
        score.setTeam2SetsWon(dto.getTeam2SetsWon());
        score.setCurrentSet(dto.getCurrentSet());
        score.setTeam1Points(dto.getTeam1Points());
        score.setTeam2Points(dto.getTeam2Points());
    }
}