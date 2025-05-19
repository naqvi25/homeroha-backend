package com.homeroha.controller;

import com.homeroha.dto.InvitationRequestDTO;
import com.homeroha.exception.HomerohaException;
import com.homeroha.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/send")
    public ResponseEntity<?> sendInvitation(@RequestBody InvitationRequestDTO request, Principal principal) {
        try {
            invitationService.sendInvitation(principal.getName(), request);
            return ResponseEntity.ok("Invitation sent successfully");
        } catch (HomerohaException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/my")
    public ResponseEntity<?> getMyInvitations(Principal principal) {
        return ResponseEntity.ok(invitationService.getPendingInvitationsForUser(principal.getName()));
    }

    @PostMapping("/accept/{inviteId}")
    public ResponseEntity<?> acceptInvitation(@PathVariable UUID inviteId, Principal principal) {
        try {
            invitationService.acceptInvitation(inviteId, principal.getName());
            return ResponseEntity.ok("Invitation accepted successfully");
        } catch (HomerohaException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/decline/{inviteId}")
    public ResponseEntity<?> declineInvitation(@PathVariable UUID inviteId, Principal principal) {
        try {
            invitationService.declineInvitation(inviteId, principal.getName());
            return ResponseEntity.ok("Invitation declined successfully");
        } catch (HomerohaException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @DeleteMapping("/cancel/{inviteId}")
    public ResponseEntity<?> cancelInvitation(@PathVariable UUID inviteId, Principal principal) {
        try {
            invitationService.cancelInvitation(inviteId, principal.getName());
            return ResponseEntity.ok("Invitation cancelled successfully");
        } catch (HomerohaException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
