package com.mits.controller.score;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.dto.score.VolleyballScoreRequestDTO;
import com.mits.entity.score.VolleyballScore;
import com.mits.service.score.VolleyballScoreService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/volleyball-scores")
public class VolleyballScoreController {

    private final VolleyballScoreService service;

    public VolleyballScoreController(VolleyballScoreService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VolleyballScore> create(@Valid @RequestBody VolleyballScoreRequestDTO dto) {
        return new ResponseEntity<>(service.createScore(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VolleyballScore>> getAll() {
        return ResponseEntity.ok(service.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolleyballScore> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getScoreById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VolleyballScore> update(@PathVariable Long id, @Valid @RequestBody VolleyballScoreRequestDTO dto) {
        return ResponseEntity.ok(service.updateScore(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteScore(id);
        return ResponseEntity.ok("Volleyball Score Deleted Successfully.");
    }
}