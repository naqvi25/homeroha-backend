package com.homeroha.controller;

import com.homeroha.dto.HomeRequestDTO;
import com.homeroha.dto.HomeResponseDTO;
import com.homeroha.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/homes")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @PostMapping("/create")
    public ResponseEntity<?> createHome(@RequestBody HomeRequestDTO request, Principal principal) {
        System.out.println("==== Home creation endpoint hit ====");
        try {
            homeService.createHome(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Home created successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/active/{homeId}")
    public ResponseEntity<?> setActiveHome(@PathVariable Long homeId, Principal principal) {
        try {
            homeService.setActiveHomeForUser(principal.getName(), homeId);
            return ResponseEntity.ok(Map.of("message", "Active home updated successfully", "homeId", homeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveHome(Principal principal) {
        try {
            System.out.println("Active home endpoint hit: " + homeService.getActiveHomeForUser(principal.getName()));
            return ResponseEntity.ok(homeService.getActiveHomeForUser(principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/myHomesList")
    public ResponseEntity<List<HomeResponseDTO>> getMyHomes(Principal principal) {
        String email = principal.getName();
        System.out.println("Logged in user: " + email);
        return ResponseEntity.ok(homeService.getHomesForUser(email));
    }

    @GetMapping("/{homeId}")
    public ResponseEntity<?> getHomeDetails(@PathVariable Long homeId, Principal principal) {
        try {
            return ResponseEntity.ok(homeService.getHomeDetails(principal.getName(), homeId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{homeId}")
    public ResponseEntity<?> deleteHome(@PathVariable Long homeId, Principal principal) {
        try {
            homeService.deleteHome(principal.getName(), homeId);
            return ResponseEntity.ok(Map.of("message", "Home deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
