package com.mits.entity;

import jakarta.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = true)
    private String shortName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    public Team() {
    }

    public Team(Long id, String teamName, Sport sport) {
        this.id = id;
        this.teamName = teamName;
        this.sport = sport;
    }

    public Team(Long id, String teamName, String shortName, Sport sport) {
        this.id = id;
        this.teamName = teamName;
        this.shortName = shortName;
        this.sport = sport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
}