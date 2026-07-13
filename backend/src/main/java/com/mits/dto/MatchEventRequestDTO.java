package com.mits.dto;

import jakarta.validation.constraints.NotNull;

public class MatchEventRequestDTO {

    @NotNull(message = "Match ID is required")
    private Long matchId;

    @NotNull(message = "Event Type is required")
    private String eventType; 

    private Long playerId;
    private Long teamId;
    private String description;
    
    // ✅ ADDED: So the admin can pass overs/time
    private String eventTime; 

    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // ✅ ADDED: Getters and Setters for eventTime
    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }
}