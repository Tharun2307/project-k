package com.mits.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mits.entity.MatchEvent;
import com.mits.repository.MatchEventRepository;
import com.mits.service.AuditLogService;
import com.mits.service.MatchEventService;
import com.mits.service.score.ScoreUpdateService;

@Service
public class MatchEventServiceImpl implements MatchEventService {

    private final MatchEventRepository matchEventRepository;
    private final ScoreUpdateService scoreUpdateService;
    private final AuditLogService auditLogService;

    public MatchEventServiceImpl(
            MatchEventRepository matchEventRepository,
            ScoreUpdateService scoreUpdateService,
            AuditLogService auditLogService) {

        this.matchEventRepository = matchEventRepository;
        this.scoreUpdateService = scoreUpdateService;
        this.auditLogService = auditLogService;
    }

    @Override
    public MatchEvent createMatchEvent(MatchEvent matchEvent) {

        // Save Match Event
        MatchEvent savedEvent = matchEventRepository.save(matchEvent);

        // Automatically update score
        scoreUpdateService.updateScore(savedEvent);

        // Save audit log
        auditLogService.log(
                "Match Event Created : " + savedEvent.getEventType(),
                "SYSTEM");

        return savedEvent;
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

        MatchEvent existingEvent =
                matchEventRepository.findById(id).orElse(null);

        if (existingEvent != null) {

            existingEvent.setEventType(matchEvent.getEventType());
            existingEvent.setEventTime(matchEvent.getEventTime());
            existingEvent.setDescription(matchEvent.getDescription());
            existingEvent.setTeam(matchEvent.getTeam());
            existingEvent.setMatch(matchEvent.getMatch());
            existingEvent.setPlayer(matchEvent.getPlayer());

            MatchEvent updatedEvent =
                    matchEventRepository.save(existingEvent);

            auditLogService.log(
                    "Match Event Updated : " + updatedEvent.getEventType(),
                    "SYSTEM");

            return updatedEvent;
        }

        return null;
    }

    @Override
    public void deleteMatchEvent(Long id) {

        matchEventRepository.deleteById(id);

        auditLogService.log(
                "Match Event Deleted : ID " + id,
                "SYSTEM");
    }
}