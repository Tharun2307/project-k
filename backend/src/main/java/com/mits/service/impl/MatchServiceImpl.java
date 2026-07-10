package com.mits.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.Match;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.MatchRepository;
import com.mits.service.MatchService;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match getMatchById(Long id) {
        // ✅ Throws 404 if not found
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match updateMatch(Long id, Match match) {
        // ✅ Throws 404 if not found
        Match existingMatch = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", id));

        existingMatch.setSport(match.getSport());
        existingMatch.setTeam1(match.getTeam1());
        existingMatch.setTeam2(match.getTeam2());
        existingMatch.setMatchDate(match.getMatchDate());
        existingMatch.setStatus(match.getStatus());

        return matchRepository.save(existingMatch);
    }

    @Override
    public void deleteMatch(Long id) {
        // ✅ Throws 404 if trying to delete a non-existent match
        if (!matchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Match", "id", id);
        }
        matchRepository.deleteById(id);
    }
}