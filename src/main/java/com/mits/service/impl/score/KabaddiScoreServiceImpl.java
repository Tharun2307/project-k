package com.mits.service.impl.score;

import java.util.List;
import org.springframework.stereotype.Service;

import com.mits.dto.score.KabaddiScoreRequestDTO;
import com.mits.entity.Match;
import com.mits.entity.score.KabaddiScore;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.MatchRepository;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.service.score.KabaddiScoreService;

@Service
public class KabaddiScoreServiceImpl implements KabaddiScoreService {

    private final KabaddiScoreRepository repository;
    private final MatchRepository matchRepository; // Added to fetch Match by ID

    public KabaddiScoreServiceImpl(KabaddiScoreRepository repository, MatchRepository matchRepository) {
        this.repository = repository;
        this.matchRepository = matchRepository;
    }

    @Override
    public KabaddiScore createScore(KabaddiScoreRequestDTO dto) {
        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        KabaddiScore score = new KabaddiScore();
        score.setMatch(match);
        mapDtoToEntity(dto, score);
        
        return repository.save(score);
    }

    @Override
    public KabaddiScore getScoreById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KabaddiScore", "id", id));
    }

    @Override
    public List<KabaddiScore> getAllScores() {
        return repository.findAll();
    }

    @Override
    public KabaddiScore updateScore(Long id, KabaddiScoreRequestDTO dto) {
        KabaddiScore existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KabaddiScore", "id", id));

        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        existing.setMatch(match);
        mapDtoToEntity(dto, existing);

        return repository.save(existing);
    }

    @Override
    public void deleteScore(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("KabaddiScore", "id", id);
        }
        repository.deleteById(id);
    }

    // Helper method to map DTO fields to Entity fields
    private void mapDtoToEntity(KabaddiScoreRequestDTO dto, KabaddiScore score) {
        score.setTeam1Points(dto.getTeam1Points());
        score.setTeam2Points(dto.getTeam2Points());
        score.setTeam1Raids(dto.getTeam1Raids());       // ✅ Added (was missing in original code)
        score.setTeam2Raids(dto.getTeam2Raids());       // ✅ Added (was missing in original code)
        score.setTeam1BonusPoints(dto.getTeam1BonusPoints());
        score.setTeam2BonusPoints(dto.getTeam2BonusPoints());
        score.setTeam1SuperTackles(dto.getTeam1SuperTackles());
        score.setTeam2SuperTackles(dto.getTeam2SuperTackles());
    }
}