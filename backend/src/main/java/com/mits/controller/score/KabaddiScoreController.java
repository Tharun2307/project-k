package com.mits.controller.score;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mits.dto.score.KabaddiScoreRequestDTO;
import com.mits.entity.score.KabaddiScore;
import com.mits.service.score.KabaddiScoreService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/kabaddi-scores")
public class KabaddiScoreController {

    private final KabaddiScoreService service;

    public KabaddiScoreController(KabaddiScoreService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<KabaddiScore> create(@Valid @RequestBody KabaddiScoreRequestDTO dto) {
        return new ResponseEntity<>(service.createScore(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<KabaddiScore>> getAll() {
        return ResponseEntity.ok(service.getAllScores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KabaddiScore> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getScoreById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KabaddiScore> update(@PathVariable Long id, @Valid @RequestBody KabaddiScoreRequestDTO dto) {
        return ResponseEntity.ok(service.updateScore(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteScore(id);
        return ResponseEntity.ok("Kabaddi Score deleted successfully.");
    }
}