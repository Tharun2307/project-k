package com.mits.service.impl.score;

import java.util.List;
import org.springframework.stereotype.Service;

import com.mits.dto.score.BadmintonScoreRequestDTO;
import com.mits.entity.Match;
import com.mits.entity.score.BadmintonScore;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.MatchRepository;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.service.score.BadmintonScoreService;

@Service
public class BadmintonScoreServiceImpl implements BadmintonScoreService {

    private final BadmintonScoreRepository repository;
    private final MatchRepository matchRepository; // Added to fetch Match by ID

    public BadmintonScoreServiceImpl(BadmintonScoreRepository repository, MatchRepository matchRepository) {
        this.repository = repository;
        this.matchRepository = matchRepository;
    }

    @Override
    public BadmintonScore createScore(BadmintonScoreRequestDTO dto) {
        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        BadmintonScore score = new BadmintonScore();
        score.setMatch(match);
        mapDtoToEntity(dto, score);
        
        return repository.save(score);
    }

    @Override
    public BadmintonScore getScoreById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BadmintonScore", "id", id));
    }

    @Override
    public List<BadmintonScore> getAllScores() {
        return repository.findAll();
    }

    @Override
    public BadmintonScore updateScore(Long id, BadmintonScoreRequestDTO dto) {
        BadmintonScore existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BadmintonScore", "id", id));

        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        existing.setMatch(match);
        mapDtoToEntity(dto, existing);

        return repository.save(existing);
    }

    @Override
    public void deleteScore(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("BadmintonScore", "id", id);
        }
        repository.deleteById(id);
    }

    // Helper method to map DTO fields to Entity fields
    private void mapDtoToEntity(BadmintonScoreRequestDTO dto, BadmintonScore score) {
        score.setPlayer1SetsWon(dto.getPlayer1SetsWon());     // ✅ Added (was missing in original code)
        score.setPlayer2SetsWon(dto.getPlayer2SetsWon());     // ✅ Added (was missing in original code)
        score.setCurrentSet(dto.getCurrentSet());             // ✅ Added (was missing in original code)
        score.setPlayer1Points(dto.getPlayer1Points());
        score.setPlayer2Points(dto.getPlayer2Points());
    }
}