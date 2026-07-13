package com.mits.service;

import com.mits.dto.MatchEventRequestDTO;
import com.mits.entity.MatchEvent;
import com.mits.entity.User;

public interface MatchEventService {
    MatchEvent recordEvent(MatchEventRequestDTO dto, User admin);
}