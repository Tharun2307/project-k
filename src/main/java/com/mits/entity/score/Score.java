package com.mits.entity.score;

import com.mits.entity.Match;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    public Score() {
    }

    public Score(Long id, Match match) {
        this.id = id;
        this.match = match;
    }

    public Long getId() {
        return id;
    }

    public Match getMatch() {
        return match;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}