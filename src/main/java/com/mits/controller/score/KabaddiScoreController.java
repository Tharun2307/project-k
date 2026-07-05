package com.mits.controller.score;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.score.KabaddiScore;
import com.mits.service.score.KabaddiScoreService;

@RestController
@RequestMapping("/api/kabaddi-scores")
public class KabaddiScoreController {

    private final KabaddiScoreService service;

    public KabaddiScoreController(KabaddiScoreService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<KabaddiScore> create(
            @RequestBody KabaddiScore score) {

        return new ResponseEntity<>(
                service.createScore(score),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<KabaddiScore>> getAll() {
        return ResponseEntity.ok(service.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KabaddiScore> getById(
            @PathVariable Long id) {

        KabaddiScore score = service.getScoreById(id);

        if (score == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(score);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KabaddiScore> update(
            @PathVariable Long id,
            @RequestBody KabaddiScore score) {

        KabaddiScore updated =
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

        return ResponseEntity.ok("Kabaddi Score deleted successfully.");
    }
}