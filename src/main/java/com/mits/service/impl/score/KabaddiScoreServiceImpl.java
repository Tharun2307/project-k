package com.mits.service.impl.score;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.score.KabaddiScore;
import com.mits.repository.score.KabaddiScoreRepository;
import com.mits.service.score.KabaddiScoreService;

@Service
public class KabaddiScoreServiceImpl
        implements KabaddiScoreService {

    private final KabaddiScoreRepository repository;

    public KabaddiScoreServiceImpl(KabaddiScoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public KabaddiScore createScore(KabaddiScore score) {
        return repository.save(score);
    }

    @Override
    public KabaddiScore getScoreById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<KabaddiScore> getAllScores() {
        return repository.findAll();
    }

    @Override
    public KabaddiScore updateScore(Long id, KabaddiScore score) {

        KabaddiScore existing =
                repository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setMatch(score.getMatch());

        existing.setTeam1Points(score.getTeam1Points());
        existing.setTeam2Points(score.getTeam2Points());

        existing.setTeam1BonusPoints(score.getTeam1BonusPoints());
        existing.setTeam2BonusPoints(score.getTeam2BonusPoints());

        existing.setTeam1SuperTackles(score.getTeam1SuperTackles());
        existing.setTeam2SuperTackles(score.getTeam2SuperTackles());

        return repository.save(existing);
    }

    @Override
    public void deleteScore(Long id) {
        repository.deleteById(id);
    }
}