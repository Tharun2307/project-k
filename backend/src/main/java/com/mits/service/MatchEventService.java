package com.mits.service;

import java.util.List;
import com.mits.entity.MatchEvent;
import com.mits.dto.MatchEventRequestDTO;

public interface MatchEventService {

    MatchEvent createMatchEvent(MatchEvent matchEvent);
    MatchEvent createMatchEventFromDTO(MatchEventRequestDTO dto);

    MatchEvent getMatchEventById(Long id);

    List<MatchEvent> getAllMatchEvents();

    // ✅ UPDATED: Now accepts DTO instead of Entity
    MatchEvent updateMatchEvent(Long id, MatchEventRequestDTO dto);

    void deleteMatchEvent(Long id);
}