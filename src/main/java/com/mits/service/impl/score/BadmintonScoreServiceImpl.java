package com.mits.service.impl.score;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.score.BadmintonScore;
import com.mits.repository.score.BadmintonScoreRepository;
import com.mits.service.score.BadmintonScoreService;

@Service
public class BadmintonScoreServiceImpl
        implements BadmintonScoreService {

    private final BadmintonScoreRepository repository;

    public BadmintonScoreServiceImpl(BadmintonScoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public BadmintonScore createScore(BadmintonScore score) {
        return repository.save(score);
    }

    @Override
    public BadmintonScore getScoreById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<BadmintonScore> getAllScores() {
        return repository.findAll();
    }

    @Override
    public BadmintonScore updateScore(Long id, BadmintonScore score) {

        BadmintonScore existing =
                repository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setMatch(score.getMatch());
        existing.setPlayer1Points(score.getPlayer1Points());
        existing.setPlayer2Points(score.getPlayer2Points());

        return repository.save(existing);
    }

    @Override
    public void deleteScore(Long id) {
        repository.deleteById(id);
    }
}