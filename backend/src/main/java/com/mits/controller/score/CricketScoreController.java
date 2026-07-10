package com.mits.controller.score;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.dto.score.CricketScoreRequestDTO;
import com.mits.entity.score.CricketScore;
import com.mits.service.score.CricketScoreService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cricket-scores")
public class CricketScoreController {

    private final CricketScoreService service;

    public CricketScoreController(CricketScoreService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CricketScore> create(@Valid @RequestBody CricketScoreRequestDTO dto) {
        return new ResponseEntity<>(service.createScore(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CricketScore>> getAll() {
        return ResponseEntity.ok(service.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CricketScore> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getScoreById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CricketScore> update(@PathVariable Long id, @Valid @RequestBody CricketScoreRequestDTO dto) {
        return ResponseEntity.ok(service.updateScore(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteScore(id);
        return ResponseEntity.ok("Cricket Score deleted successfully.");
    }
}