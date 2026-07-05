package com.mits.service.impl.score;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.score.CricketScore;
import com.mits.repository.score.CricketScoreRepository;
import com.mits.service.score.CricketScoreService;

@Service
public class CricketScoreServiceImpl implements CricketScoreService {

    private final CricketScoreRepository repository;

    public CricketScoreServiceImpl(CricketScoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public CricketScore createScore(CricketScore score) {
        return repository.save(score);
    }

    @Override
    public CricketScore getScoreById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<CricketScore> getAllScores() {
        return repository.findAll();
    }

    @Override
    public CricketScore updateScore(Long id, CricketScore score) {

        CricketScore existing = repository.findById(id).orElse(null);

        if (existing != null) {

            existing.setMatch(score.getMatch());
            existing.setRuns(score.getRuns());
            existing.setWickets(score.getWickets());
            existing.setOvers(score.getOvers());
            existing.setExtras(score.getExtras());
            existing.setTarget(score.getTarget());
            existing.setInnings(score.getInnings());

            return repository.save(existing);
        }

        return null;
    }

    @Override
    public void deleteScore(Long id) {
        repository.deleteById(id);
    }
}