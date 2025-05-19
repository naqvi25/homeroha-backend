package com.homeroha.controller;

import com.homeroha.dto.NoteRequestDTO;
import com.homeroha.dto.NoteResponseDTO;
import com.homeroha.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/add")
    public ResponseEntity<NoteResponseDTO> addNote(@RequestBody NoteRequestDTO request, Principal principal) {
        return ResponseEntity.ok(noteService.addNote(principal.getName(), request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<NoteResponseDTO>> getNotes(Principal principal) {
        return ResponseEntity.ok(noteService.getNotes(principal.getName()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<NoteResponseDTO> updateNote(@PathVariable Long id, @RequestBody NoteRequestDTO request, Principal principal) {
        return ResponseEntity.ok(noteService.updateNote(id, principal.getName(), request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, Principal principal) {
        noteService.deleteNote(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Note deleted", "noteId", id));
    }
}
