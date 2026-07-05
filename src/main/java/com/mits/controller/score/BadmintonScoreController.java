package com.mits.controller.score;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.score.BadmintonScore;
import com.mits.service.score.BadmintonScoreService;

@RestController
@RequestMapping("/api/badminton-scores")
public class BadmintonScoreController {

    private final BadmintonScoreService service;

    public BadmintonScoreController(BadmintonScoreService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BadmintonScore> create(
            @RequestBody BadmintonScore score) {

        return new ResponseEntity<>(
                service.createScore(score),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BadmintonScore>> getAll() {
        return ResponseEntity.ok(service.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadmintonScore> getById(
            @PathVariable Long id) {

        BadmintonScore score = service.getScoreById(id);

        if (score == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(score);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BadmintonScore> update(
            @PathVariable Long id,
            @RequestBody BadmintonScore score) {

        BadmintonScore updated =
                service.updateScore(id, score);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        service.deleteScore(id);

        return ResponseEntity.ok("Badminton Score deleted successfully.");
    }
}