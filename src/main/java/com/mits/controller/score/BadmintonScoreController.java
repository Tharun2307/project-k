package com.mits.controller.score;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.dto.score.BadmintonScoreRequestDTO;
import com.mits.entity.score.BadmintonScore;
import com.mits.service.score.BadmintonScoreService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/badminton-scores")
public class BadmintonScoreController {

    private final BadmintonScoreService service;

    public BadmintonScoreController(BadmintonScoreService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BadmintonScore> create(@Valid @RequestBody BadmintonScoreRequestDTO dto) {
        return new ResponseEntity<>(service.createScore(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BadmintonScore>> getAll() {
        return ResponseEntity.ok(service.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadmintonScore> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getScoreById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BadmintonScore> update(@PathVariable Long id, @Valid @RequestBody BadmintonScoreRequestDTO dto) {
        return ResponseEntity.ok(service.updateScore(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteScore(id);
        return ResponseEntity.ok("Badminton Score deleted successfully.");
    }
}