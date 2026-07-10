package com.mits.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mits.dto.MatchEventRequestDTO;
import com.mits.dto.LiveScoreResponse;
import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.Player;
import com.mits.entity.Team;
// ✅ REMOVED: import com.mits.enums.EventType;
import com.mits.exception.ResourceNotFoundException;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.MatchRepository;
import com.mits.repository.PlayerRepository;
import com.mits.repository.TeamRepository;
import com.mits.service.AuditLogService;
import com.mits.service.ScoreBroadcastService;
import com.mits.service.MatchEventService;
import com.mits.service.score.LiveScoreService;
import com.mits.service.score.ScoreUpdateService;

@Service
public class MatchEventServiceImpl implements MatchEventService {

    private final MatchEventRepository matchEventRepository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final ScoreUpdateService scoreUpdateService;
    private final AuditLogService auditLogService;
    private final LiveScoreService liveScoreService;
    private final ScoreBroadcastService scoreBroadcastService;

    public MatchEventServiceImpl(
            MatchEventRepository matchEventRepository,
            MatchRepository matchRepository,
            PlayerRepository playerRepository,
            TeamRepository teamRepository,
            ScoreUpdateService scoreUpdateService,
            AuditLogService auditLogService,
            LiveScoreService liveScoreService,
            ScoreBroadcastService scoreBroadcastService) {

        this.matchEventRepository = matchEventRepository;
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.scoreUpdateService = scoreUpdateService;
        this.auditLogService = auditLogService;
        this.liveScoreService = liveScoreService;
        this.scoreBroadcastService = scoreBroadcastService;
    }

    @Override
    @Transactional
    public MatchEvent createMatchEventFromDTO(MatchEventRequestDTO dto) {
        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        Player player = null;
        if (dto.getPlayerId() != null) {
            player = playerRepository.findById(dto.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player", "id", dto.getPlayerId()));
        }

        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeamId()));

        MatchEvent event = new MatchEvent();
        
        // ✅ FIX 1: No more EventType.valueOf(), just use the String directly
        event.setEventType(dto.getEventType()); 
        event.setEventTime(dto.getEventTime());
        event.setDescription(dto.getDescription());
        event.setMatch(match);
        event.setPlayer(player);
        event.setTeam(team);

        MatchEvent savedEvent = matchEventRepository.save(event);

        scoreUpdateService.updateScore(savedEvent);
        
        // ✅ FIX 2: Pass 5 arguments to prevent "entity_id cannot be null" error
        auditLogService.log(
                "CREATE", 
                "Match Event Created : " + savedEvent.getEventType(), 
                "MatchEvent", 
                savedEvent.getId(), 
                "SYSTEM");

        try {
            LiveScoreResponse liveScore = liveScoreService.getLiveScoreByMatchId(savedEvent.getMatch().getId());
            scoreBroadcastService.broadcastScoreUpdate(savedEvent.getMatch().getId(), liveScore);
        } catch (Exception e) {
            System.err.println("❌ Failed to broadcast score: " + e.getMessage());
        }

        return savedEvent;
    }

    @Override
    public MatchEvent createMatchEvent(MatchEvent matchEvent) {
        MatchEvent savedEvent = matchEventRepository.save(matchEvent);
        scoreUpdateService.updateScore(savedEvent);
        
        // ✅ FIX 2: Updated audit log signature
        auditLogService.log(
                "CREATE", 
                "Match Event Created : " + savedEvent.getEventType(), 
                "MatchEvent", 
                savedEvent.getId(), 
                "SYSTEM");
                
        return savedEvent;
    }

    @Override
    public MatchEvent getMatchEventById(Long id) {
        return matchEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MatchEvent", "id", id));
    }

    @Override
    public List<MatchEvent> getAllMatchEvents() {
        return matchEventRepository.findAll();
    }

    @Override
    @Transactional
    public MatchEvent updateMatchEvent(Long id, MatchEventRequestDTO dto) {
        MatchEvent existingEvent = matchEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MatchEvent", "id", id));

        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", dto.getMatchId()));

        Player player = null;
        if (dto.getPlayerId() != null) {
            player = playerRepository.findById(dto.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player", "id", dto.getPlayerId()));
        }

        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", dto.getTeamId()));

        // Update fields
        // ✅ FIX 1: No more EventType.valueOf()
        existingEvent.setEventType(dto.getEventType()); 
        existingEvent.setEventTime(dto.getEventTime());
        existingEvent.setDescription(dto.getDescription());
        existingEvent.setMatch(match);
        existingEvent.setPlayer(player);
        existingEvent.setTeam(team);

        MatchEvent updatedEvent = matchEventRepository.save(existingEvent);

        scoreUpdateService.updateScore(updatedEvent);

        // ✅ FIX 2: Updated audit log signature
        auditLogService.log(
                "UPDATE", 
                "Match Event Updated : " + updatedEvent.getEventType(), 
                "MatchEvent", 
                updatedEvent.getId(), 
                "SYSTEM");

        try {
            LiveScoreResponse liveScore = liveScoreService.getLiveScoreByMatchId(updatedEvent.getMatch().getId());
            scoreBroadcastService.broadcastScoreUpdate(updatedEvent.getMatch().getId(), liveScore);
        } catch (Exception e) {
            System.err.println("❌ Failed to broadcast score: " + e.getMessage());
        }

        return updatedEvent;
    }

    @Override
    @Transactional
    public void deleteMatchEvent(Long id) {
        if (!matchEventRepository.existsById(id)) {
            throw new ResourceNotFoundException("MatchEvent", "id", id);
        }

        // ✅ FIX 2: Updated audit log signature
        auditLogService.log(
                "DELETE", 
                "Match Event Deleted", 
                "MatchEvent", 
                id, 
                "SYSTEM");

        matchEventRepository.deleteById(id);
    }
}