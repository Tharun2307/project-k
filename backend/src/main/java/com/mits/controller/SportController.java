package com.mits.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Sport;
import com.mits.service.SportService;

@RestController
@RequestMapping("/api/sports") // ✅ Public access for viewing
public class SportController {

    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    // Get All Sports (Public)
    @GetMapping
    public ResponseEntity<List<Sport>> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSports());
    }

    // Get Sport by ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<Sport> getSportById(@PathVariable Long id) {
        Sport sport = sportService.getSportById(id);
        return ResponseEntity.ok(sport);
    }
}