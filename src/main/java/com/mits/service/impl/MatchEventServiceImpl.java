package com.mits.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.MatchEvent;
import com.mits.repository.MatchEventRepository;
import com.mits.service.MatchEventService;

@Service
public class MatchEventServiceImpl implements MatchEventService {

    private final MatchEventRepository matchEventRepository;

    public MatchEventServiceImpl(MatchEventRepository matchEventRepository) {
        this.matchEventRepository = matchEventRepository;
    }

    @Override
    public MatchEvent createMatchEvent(MatchEvent matchEvent) {
        return matchEventRepository.save(matchEvent);
    }

    @Override
    public MatchEvent getMatchEventById(Long id) {
        return matchEventRepository.findById(id).orElse(null);
    }

    @Override
    public List<MatchEvent> getAllMatchEvents() {
        return matchEventRepository.findAll();
    }

    @Override
    public MatchEvent updateMatchEvent(Long id, MatchEvent matchEvent) {

        MatchEvent existingEvent = matchEventRepository.findById(id).orElse(null);

        if (existingEvent != null) {

            existingEvent.setEventType(matchEvent.getEventType());
            existingEvent.setMinute(matchEvent.getMinute());
            existingEvent.setMatch(matchEvent.getMatch());
            existingEvent.setPlayer(matchEvent.getPlayer());

            return matchEventRepository.save(existingEvent);
        }

        return null;
    }

    @Override
    public void deleteMatchEvent(Long id) {
        matchEventRepository.deleteById(id);
    }
}