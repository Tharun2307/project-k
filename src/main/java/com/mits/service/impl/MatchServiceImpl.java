package com.mits.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.Match;
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
        return matchRepository.findById(id).orElse(null);
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match updateMatch(Long id, Match match) {

        Match existingMatch = matchRepository.findById(id).orElse(null);

        if (existingMatch != null) {

            existingMatch.setSport(match.getSport());
            existingMatch.setTeam1(match.getTeam1());
            existingMatch.setTeam2(match.getTeam2());
            existingMatch.setMatchDate(match.getMatchDate());
            existingMatch.setStatus(match.getStatus());

            return matchRepository.save(existingMatch);
        }

        return null;
    }

    @Override
    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }
}