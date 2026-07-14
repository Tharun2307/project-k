package com.mits.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(nullable = false)
    private String eventType; 

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private String description;
    private String eventTime; 

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // --- KABADDI SPECIFIC FIELDS ---
    private Integer tackledDefendersCount;
    private Boolean isBonusCrossed;
    private Integer defendersOnCourt;
    private Boolean isDoOrDie;

    public MatchEvent() {
        this.timestamp = LocalDateTime.now();
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Integer getTackledDefendersCount() { return tackledDefendersCount; }
    public void setTackledDefendersCount(Integer tackledDefendersCount) { this.tackledDefendersCount = tackledDefendersCount; }

    public Boolean getIsBonusCrossed() { return isBonusCrossed; }
    public void setIsBonusCrossed(Boolean isBonusCrossed) { this.isBonusCrossed = isBonusCrossed; }

    public Integer getDefendersOnCourt() { return defendersOnCourt; }
    public void setDefendersOnCourt(Integer defendersOnCourt) { this.defendersOnCourt = defendersOnCourt; }

    public Boolean getIsDoOrDie() { return isDoOrDie; }
    public void setIsDoOrDie(Boolean isDoOrDie) { this.isDoOrDie = isDoOrDie; }
}