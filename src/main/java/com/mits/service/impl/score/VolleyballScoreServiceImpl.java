package com.mits.service.impl.score;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.score.VolleyballScore;
import com.mits.repository.score.VolleyballScoreRepository;
import com.mits.service.score.VolleyballScoreService;

@Service
public class VolleyballScoreServiceImpl
        implements VolleyballScoreService {

    private final VolleyballScoreRepository repository;

    public VolleyballScoreServiceImpl(
            VolleyballScoreRepository repository) {

        this.repository = repository;
    }

    @Override
    public VolleyballScore createScore(VolleyballScore score) {
        return repository.save(score);
    }

    @Override
    public VolleyballScore getScoreById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<VolleyballScore> getAllScores() {
        return repository.findAll();
    }

    @Override
    public VolleyballScore updateScore(
            Long id,
            VolleyballScore score) {

        VolleyballScore existing =
                repository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setMatch(score.getMatch());
        existing.setTeam1Points(score.getTeam1Points());
        existing.setTeam2Points(score.getTeam2Points());

        return repository.save(existing);
    }

    @Override
    public void deleteScore(Long id) {
        repository.deleteById(id);
    }
}