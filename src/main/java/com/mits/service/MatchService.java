package com.mits.service;

import java.util.List;

import com.mits.entity.Match;

public interface MatchService {

    Match createMatch(Match match);

    Match getMatchById(Long id);

    List<Match> getAllMatches();

    Match updateMatch(Long id, Match match);

    void deleteMatch(Long id);

}