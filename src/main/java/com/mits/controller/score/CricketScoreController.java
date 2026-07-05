package com.mits.controller.score;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.entity.score.CricketScore;
import com.mits.service.score.CricketScoreService;

@RestController
@RequestMapping("/api/cricket-scores")
public class CricketScoreController {

    private final CricketScoreService service;

    public CricketScoreController(CricketScoreService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CricketScore> create(@RequestBody CricketScore score) {
        return new ResponseEntity<>(service.createScore(score), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CricketScore>> getAll() {
        return ResponseEntity.ok(service.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CricketScore> getById(@PathVariable Long id) {

        CricketScore score = service.getScoreById(id);

        if (score == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(score);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CricketScore> update(@PathVariable Long id,
                                               @RequestBody CricketScore score) {

        CricketScore updated = service.updateScore(id, score);

        if (updated == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        service.deleteScore(id);

        return ResponseEntity.ok("Cricket Score deleted successfully.");
    }
}