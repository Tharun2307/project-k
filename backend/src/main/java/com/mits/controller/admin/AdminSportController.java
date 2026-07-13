package com.mits.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.Sport;
import com.mits.service.SportService;
import com.mits.dto.SportRequestDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/sports") // ✅ Protected by EVENT_COORDINATOR role
public class AdminSportController {

    private final SportService sportService;

    public AdminSportController(SportService sportService) {
        this.sportService = sportService;
    }

    // Create Sport (Event Coordinator Only)
    @PostMapping
    public ResponseEntity<Sport> createSport(@Valid @RequestBody SportRequestDTO dto) {
        Sport sport = new Sport();
        sport.setSportName(dto.getSportName());
        sport.setDescription(dto.getDescription());
        
        Sport savedSport = sportService.createSport(sport);
        return ResponseEntity.ok(savedSport);
    }

    // Update Sport (Event Coordinator Only)
    @PutMapping("/{id}")
    public ResponseEntity<Sport> updateSport(@PathVariable Long id, @Valid @RequestBody SportRequestDTO dto) {
        // Fetch existing sport first
        Sport existingSport = sportService.getSportById(id);
        
        // Update fields from DTO
        existingSport.setSportName(dto.getSportName());
        existingSport.setDescription(dto.getDescription());
        
        Sport updatedSport = sportService.updateSport(id, existingSport);
        return ResponseEntity.ok(updatedSport);
    }

    // Delete Sport (Event Coordinator Only)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSport(@PathVariable Long id) {
        sportService.deleteSport(id);
        return ResponseEntity.ok("Sport deleted successfully.");
    }
}