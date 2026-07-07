package com.mits.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mits.dto.MatchEventRequestDTO;
import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.Player;
import com.mits.entity.Team;
import com.mits.enums.EventType;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.MatchRepository;
import com.mits.repository.PlayerRepository;
import com.mits.repository.TeamRepository;
import com.mits.service.AuditLogService;
import com.mits.service.MatchEventService;
import com.mits.service.score.ScoreUpdateService;

@Service
public class MatchEventServiceImpl implements MatchEventService {

    private final MatchEventRepository matchEventRepository;
    private final MatchRepository matchRepository;         // ✅ Added for DTO method
    private final PlayerRepository playerRepository;       // ✅ Added for DTO method
    private final TeamRepository teamRepository;           // ✅ Added for DTO method
    private final ScoreUpdateService scoreUpdateService;
    private final AuditLogService auditLogService;

    // ✅ Updated Constructor with new repositories
    public MatchEventServiceImpl(
            MatchEventRepository matchEventRepository,
            MatchRepository matchRepository,
            PlayerRepository playerRepository,
            TeamRepository teamRepository,
            ScoreUpdateService scoreUpdateService,
            AuditLogService auditLogService) {

        this.matchEventRepository = matchEventRepository;
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.scoreUpdateService = scoreUpdateService;
        this.auditLogService = auditLogService;
    }

    // ✅ NEW METHOD: Handles the DTO from the controller
    @Override
    @Transactional
    public MatchEvent createMatchEventFromDTO(MatchEventRequestDTO dto) {
        
        // 1. Fetch the related entities from database using IDs
        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        Player player = null;
        if (dto.getPlayerId() != null) {
            player = playerRepository.findById(dto.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player", "id", dto.getPlayerId()));
        }

        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeamId()));

        // 2. Create the MatchEvent entity
        MatchEvent event = new MatchEvent();
        event.setEventType(EventType.valueOf(dto.getEventType().toUpperCase()));
        event.setEventTime(dto.getEventTime());
        event.setDescription(dto.getDescription());
        event.setMatch(match);
        event.setPlayer(player);
        event.setTeam(team);

        // 3. Save the event
        MatchEvent savedEvent = matchEventRepository.save(event);

        // 4. Automatically update score
        scoreUpdateService.updateScore(savedEvent);

        // 5. Save audit log
        auditLogService.log(
                "Match Event Created : " + savedEvent.getEventType(),
                "SYSTEM");

        return savedEvent;
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

    // ✅ UPDATED: Now throws ResourceNotFoundException instead of returning null
    @Override
    public MatchEvent getMatchEventById(Long id) {
        return matchEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MatchEvent", "id", id));
    }

    @Override
    public List<MatchEvent> getAllMatchEvents() {
        return matchEventRepository.findAll();
    }

    // ✅ UPDATED: Now throws ResourceNotFoundException instead of returning null
    @Override
    public MatchEvent updateMatchEvent(Long id, MatchEvent matchEvent) {

        MatchEvent existingEvent = matchEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MatchEvent", "id", id));

        existingEvent.setEventType(matchEvent.getEventType());
        existingEvent.setEventTime(matchEvent.getEventTime());
        existingEvent.setDescription(matchEvent.getDescription());
        existingEvent.setTeam(matchEvent.getTeam());
        existingEvent.setMatch(matchEvent.getMatch());
        existingEvent.setPlayer(matchEvent.getPlayer());

        MatchEvent updatedEvent = matchEventRepository.save(existingEvent);

        auditLogService.log(
                "Match Event Updated : " + updatedEvent.getEventType(),
                "SYSTEM");

        return updatedEvent;
    }

    // ✅ UPDATED: Now throws ResourceNotFoundException if ID doesn't exist
    @Override
    public void deleteMatchEvent(Long id) {
        
        if (!matchEventRepository.existsById(id)) {
            throw new ResourceNotFoundException("MatchEvent", "id", id);
        }

        matchEventRepository.deleteById(id);

        auditLogService.log(
                "Match Event Deleted : ID " + id,
                "SYSTEM");
    }
}