package com.mits.controller.score;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.score.VolleyballScore;
import com.mits.service.score.VolleyballScoreService;

@RestController
@RequestMapping("/api/volleyball-scores")
public class VolleyballScoreController {

    private final VolleyballScoreService service;

    public VolleyballScoreController(
            VolleyballScoreService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VolleyballScore> create(
            @RequestBody VolleyballScore score) {

        return new ResponseEntity<>(
                service.createScore(score),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VolleyballScore>> getAll() {

        return ResponseEntity.ok(
                service.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolleyballScore> getById(
            @PathVariable Long id) {

        VolleyballScore score =
                service.getScoreById(id);

        if (score == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(score);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VolleyballScore> update(
            @PathVariable Long id,
            @RequestBody VolleyballScore score) {

        VolleyballScore updated =
                service.updateScore(id, score);

        if (updated == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        service.deleteScore(id);

        return ResponseEntity.ok("Volleyball Score Deleted Successfully.");
    }
}