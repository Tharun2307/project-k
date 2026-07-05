package com.mits.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Sport;
import com.mits.service.SportService;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    // Create Sport
    @PostMapping
    public ResponseEntity<Sport> createSport(@RequestBody Sport sport) {
        Sport savedSport = sportService.createSport(sport);
        return new ResponseEntity<>(savedSport, HttpStatus.CREATED);
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

        if (sport == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sport);
    }

    // Update Sport
    @PutMapping("/{id}")
    public ResponseEntity<Sport> updateSport(@PathVariable Long id,
                                             @RequestBody Sport sport) {

        Sport updatedSport = sportService.updateSport(id, sport);

        if (updatedSport == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedSport);
    }

    // Delete Sport
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSport(@PathVariable Long id) {

        sportService.deleteSport(id);

        return ResponseEntity.ok("Sport deleted successfully.");
    }
}