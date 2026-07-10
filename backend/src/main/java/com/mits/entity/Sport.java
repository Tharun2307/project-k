package com.mits.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sportName;

    private String description;

    // ✅ NEW: Link to Users (Many-to-Many)
    @ManyToMany(mappedBy = "assignedSports")
    @JsonIgnore
    private Set<User> admins = new HashSet<>();

    public Sport() {
    }

    public Sport(Long id, String sportName, String description) {
        this.id = id;
        this.sportName = sportName;
        this.description = description;
    }

    // --- Existing Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSportName() { return sportName; }
    public void setSportName(String sportName) { this.sportName = sportName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // ✅ NEW: Getters & Setters for admins
    public Set<User> getAdmins() { return admins; }
    public void setAdmins(Set<User> admins) { this.admins = admins; }
}