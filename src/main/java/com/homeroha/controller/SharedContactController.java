package com.homeroha.controller;

import com.homeroha.dto.SharedContactRequestDTO;
import com.homeroha.service.SharedContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/shared-contacts")
@RequiredArgsConstructor
public class SharedContactController {

    private final SharedContactService sharedContactService;

    @PostMapping("/add")
    public ResponseEntity<?> addContact(@RequestBody SharedContactRequestDTO request, Principal principal) {
        try {
            return ResponseEntity.ok(sharedContactService.addContact(principal.getName(), request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listContacts(
            @RequestParam Long homeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Principal principal
    ) {
        return ResponseEntity.ok(sharedContactService.listContacts(principal.getName(), homeId, page, size, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContact(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(sharedContactService.getContact(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @RequestBody SharedContactRequestDTO request) {
        try {
            return ResponseEntity.ok(sharedContactService.updateContact(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        try {
            sharedContactService.deleteContact(id);
            return ResponseEntity.ok("Deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
