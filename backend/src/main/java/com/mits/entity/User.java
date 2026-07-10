package com.mits.entity;

import com.mits.enums.Role;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // ✅ NEW: Link to Sports (Many-to-Many)
    @ManyToMany
    @JoinTable(
        name = "user_sports", // Name of the linking table in DB
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "sport_id")
    )
    private Set<Sport> assignedSports = new HashSet<>();

    public User() {
    }

    public User(Long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // --- Existing Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // ✅ NEW: Getters & Setters for assignedSports
    public Set<Sport> getAssignedSports() { return assignedSports; }
    public void setAssignedSports(Set<Sport> assignedSports) { this.assignedSports = assignedSports; }
}