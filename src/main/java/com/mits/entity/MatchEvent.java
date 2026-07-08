package com.mits.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "match_events")
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ MUST BE STRING
    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String eventTime;

    private String description;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public MatchEvent() {
    }

    public MatchEvent(Long id,
                      String eventType,
                      String eventTime,
                      String description,
                      Match match,
                      Team team,
                      Player player) {
        this.id = id;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.description = description;
        this.match = match;
        this.team = team;
        this.player = player;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // ✅ MUST BE STRING
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
}