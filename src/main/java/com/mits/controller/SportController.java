package com.mits.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Sport;
import com.mits.service.SportService;
import com.mits.dto.SportRequestDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    // Create Sport
    @PostMapping
    public ResponseEntity<Sport> createSport(@Valid @RequestBody SportRequestDTO dto) {
        Sport sport = new Sport();
        sport.setSportName(dto.getSportName());
        sport.setDescription(dto.getDescription());
        Sport savedSport = sportService.createSport(sport);
        return ResponseEntity.ok(savedSport);
    }

    // Get All Sports
    @GetMapping
    public ResponseEntity<List<Sport>> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSports());
    }

    // Get Sport by ID
    @GetMapping("/{id}")
    public ResponseEntity<Sport> getSportById(@PathVariable Long id) {
        Sport sport = sportService.getSportById(id);
        return ResponseEntity.ok(sport);
    }

    // Update Sport
    @PutMapping("/{id}")
    public ResponseEntity<Sport> updateSport(@PathVariable Long id, @RequestBody Sport sport) {
        Sport updatedSport = sportService.updateSport(id, sport);
        return ResponseEntity.ok(updatedSport);
    }

    // Delete Sport
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSport(@PathVariable Long id) {
        sportService.deleteSport(id);
        return ResponseEntity.ok("Sport deleted successfully.");
    }
}