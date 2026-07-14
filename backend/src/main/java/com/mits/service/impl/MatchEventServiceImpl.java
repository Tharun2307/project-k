package com.mits.service.impl;

import org.springframework.stereotype.Service;

import com.mits.dto.MatchEventRequestDTO;
import com.mits.entity.Match;
import com.mits.entity.MatchEvent;
import com.mits.entity.Player;
import com.mits.entity.Team;
import com.mits.entity.User;
import com.mits.repository.MatchEventRepository;
import com.mits.repository.PlayerRepository;
import com.mits.repository.TeamRepository;
import com.mits.service.MatchEventService;
import com.mits.service.MatchService;
import com.mits.service.score.ScoreUpdateService;
import com.mits.service.ScoreBroadcastService;

@Service
public class MatchEventServiceImpl implements MatchEventService {

    private final MatchEventRepository matchEventRepository;
    private final MatchService matchService;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final ScoreUpdateService scoreUpdateService;
    private final ScoreBroadcastService scoreBroadcastService;

    public MatchEventServiceImpl(MatchEventRepository matchEventRepository, 
                                 MatchService matchService,
                                 TeamRepository teamRepository,
                                 PlayerRepository playerRepository,
                                 ScoreUpdateService scoreUpdateService,
                                 ScoreBroadcastService scoreBroadcastService) {
        this.matchEventRepository = matchEventRepository;
        this.matchService = matchService;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.scoreUpdateService = scoreUpdateService;
        this.scoreBroadcastService = scoreBroadcastService;
    }

    @Override
    public MatchEvent recordEvent(MatchEventRequestDTO dto, User admin) {
        
        Match match = matchService.getMatchById(dto.getMatchId());

        MatchEvent event = new MatchEvent();
        event.setMatch(match);
        event.setEventType(dto.getEventType());
        event.setDescription(dto.getDescription());
        event.setEventTime(dto.getEventTime());

        // ✅ CRITICAL FIX: Map the new Kabaddi fields from DTO to Entity
        event.setTackledDefendersCount(dto.getTackledDefendersCount());
        event.setIsBonusCrossed(dto.getIsBonusCrossed());
        event.setDefendersOnCourt(dto.getDefendersOnCourt());
        event.setIsDoOrDie(dto.getIsDoOrDie());

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            event.setTeam(team);
        }

        if (dto.getPlayerId() != null) {
            Player player = playerRepository.findById(dto.getPlayerId())
                    .orElseThrow(() -> new RuntimeException("Player not found"));
            event.setPlayer(player);
        }

        // 1. Save the event
        MatchEvent savedEvent = matchEventRepository.save(event);
        
        // 2. Update the actual score in the DB
        scoreUpdateService.updateScore(savedEvent);
        
        // 3. Broadcast the event via WebSocket
        scoreBroadcastService.broadcastScoreUpdate(match.getId(), savedEvent);
        
        return savedEvent;
    }
}