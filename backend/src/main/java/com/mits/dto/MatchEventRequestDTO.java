package com.mits.dto;

import jakarta.validation.constraints.NotNull;

public class MatchEventRequestDTO {

    @NotNull(message = "Match ID is required")
    private Long matchId;

    @NotNull(message = "Event Type is required") // e.g., "RAID_SUCCESS", "TACKLE", "BONUS", "ALL_OUT", "EMPTY_RAID"
    private String eventType; 

    private Long playerId;
    private Long teamId;
    private String description;
    private String eventTime; 

    // --- KABADDI SPECIFIC FIELDS ---
    private Integer tackledDefendersCount; // e.g., 2 (Raider tagged 2 people)
    private Boolean isBonusCrossed;        // true/false
    private Integer defendersOnCourt;      // e.g., 3 (Needed to calculate Super Tackle)
    private Boolean isDoOrDie;             // true/false (3rd consecutive empty raid)

    // --- GETTERS AND SETTERS ---
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

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public Integer getTackledDefendersCount() { return tackledDefendersCount; }
    public void setTackledDefendersCount(Integer tackledDefendersCount) { this.tackledDefendersCount = tackledDefendersCount; }

    public Boolean getIsBonusCrossed() { return isBonusCrossed; }
    public void setIsBonusCrossed(Boolean isBonusCrossed) { this.isBonusCrossed = isBonusCrossed; }

    public Integer getDefendersOnCourt() { return defendersOnCourt; }
    public void setDefendersOnCourt(Integer defendersOnCourt) { this.defendersOnCourt = defendersOnCourt; }

    public Boolean getIsDoOrDie() { return isDoOrDie; }
    public void setIsDoOrDie(Boolean isDoOrDie) { this.isDoOrDie = isDoOrDie; }
}