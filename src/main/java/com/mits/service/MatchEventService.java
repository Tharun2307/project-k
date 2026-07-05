package com.mits.service;

import java.util.List;

import com.mits.entity.MatchEvent;

public interface MatchEventService {

    MatchEvent createMatchEvent(MatchEvent matchEvent);

    MatchEvent getMatchEventById(Long id);

    List<MatchEvent> getAllMatchEvents();

    MatchEvent updateMatchEvent(Long id, MatchEvent matchEvent);

    void deleteMatchEvent(Long id);

}