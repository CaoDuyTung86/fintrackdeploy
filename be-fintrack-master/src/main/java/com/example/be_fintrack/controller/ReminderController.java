package com.example.be_fintrack.controller;

import com.example.be_fintrack.entity.Reminder;
import com.example.be_fintrack.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin
public class ReminderController {

    @Autowired
    private ReminderService service;

    @PostMapping
    public ResponseEntity<Reminder> create(@RequestBody Reminder r) {
        return ResponseEntity.ok(service.create(r));
    }

    @GetMapping
    public ResponseEntity<List<Reminder>> getByUsername(@RequestParam String username) {
        return ResponseEntity.ok(service.getByUsername(username));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Reminder> update(@PathVariable Long id, @RequestBody Reminder r) {
        return ResponseEntity.ok(service.update(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
