package com.homeroha.service.impl;

import com.homeroha.dto.InvitationRequestDTO;
import com.homeroha.dto.InvitationStatus;
import com.homeroha.dto.Role;
import com.homeroha.exception.HomerohaException;
import com.homeroha.model.*;
import com.homeroha.repository.*;
import com.homeroha.service.EmailService;
import com.homeroha.service.InvitationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvitationServiceImpl implements InvitationService {

    private final UserRepository userRepository;
    private final HomeRepository homeRepository;
    private final UserInvitationRepository invitationRepository;
    private final UserHomeRepository userHomeRepository;
    private final EmailService emailService;

    @Override
    public void sendInvitation(String senderEmail, InvitationRequestDTO request) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new HomerohaException("Sender not found"));

        Home home = homeRepository.findById(request.getHomeId())
                .orElseThrow(() -> new HomerohaException("Home not found"));

        // Check if sender is ADMIN of the home
        UserHome userHome = userHomeRepository.findByUserAndHome(sender, home)
                .orElseThrow(() -> new HomerohaException("Sender is not part of the home"));
        if (userHome.getRole() != Role.ADMIN) {
            throw new HomerohaException("Only Admins can send invitations");
        }

        // Prevent duplicate invites
        if (invitationRepository.existsByEmailAndHomeAndStatus(request.getEmail(), home, InvitationStatus.PENDING)) {
            throw new HomerohaException("An active invitation already exists for this user");
        }

        // Create invitation
        UserInvitation invitation = UserInvitation.builder()
                .email(request.getEmail())
                .home(home)
                .role(request.getRole())
                .status(InvitationStatus.PENDING)
                .invitedBy(sender)
                .invitedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        invitationRepository.save(invitation);

        // ✅ Send email
        String subject = "You're invited to join a home on Homeroha";
        String body = """
                <h3>Greetings!</h3>
                <p>You’ve been invited to join the home <strong>%s</strong> on Homeroha as <strong>%s</strong>.</p>
                <p>Please log in to the Homeroha app to accept or decline the invitation.</p>
                <p>This invite will expire in 7 days.</p>
                <br />
                <p>Regards,</p>
                <p>Homeroha Team</p>
                """.formatted(home.getName(), request.getRole().name());

        emailService.sendEmail(request.getEmail(), subject, body);
    }

//    @Override
//    public List<UserInvitation> getPendingInvitationsForUser(String email) {
//        return invitationRepository.findAllByEmailAndStatus(email, InvitationStatus.PENDING);
//    }

    @Override
    public List<UserInvitation> getPendingInvitationsForUser(String email) {
        List<UserInvitation> invitations = invitationRepository.findAllByEmailAndStatus(email, InvitationStatus.PENDING);

        return invitations.stream()
                .map(invite -> UserInvitation.builder()
                        .id(invite.getId())
                        .email(invite.getEmail())
                        .status(invite.getStatus())
                        .home(invite.getHome())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void acceptInvitation(UUID inviteId, String userEmail) {
        UserInvitation invitation = invitationRepository.findByIdAndEmail(inviteId, userEmail)
                .orElseThrow(() -> new HomerohaException("Invitation not found for this user"));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new HomerohaException("Invitation is not active");
        }

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new HomerohaException("Invitation has expired");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));

        // Link user to home
        UserHome userHome = UserHome.builder()
                .user(user)
                .home(invitation.getHome())
                .role(invitation.getRole())
                .build();
        userHomeRepository.save(userHome);

        // Mark invitation accepted
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
    }

    @Override
    public void declineInvitation(UUID inviteId, String email) {
        UserInvitation invitation = invitationRepository.findByIdAndEmail(inviteId, email)
                .orElseThrow(() -> new HomerohaException("Invitation not found"));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new HomerohaException("Cannot decline this invitation");
        }

        invitation.setStatus(InvitationStatus.DECLINED);
        invitationRepository.save(invitation);
    }

    @Override
    public void cancelInvitation(UUID inviteId, String senderEmail) {
        UserInvitation invitation = invitationRepository.findById(inviteId)
                .orElseThrow(() -> new HomerohaException("Invitation not found"));

        if (!invitation.getInvitedBy().getEmail().equals(senderEmail)) {
            throw new HomerohaException("Only the inviter can cancel this invitation");
        }

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new HomerohaException("Only pending invitations can be cancelled");
        }

        invitation.setStatus(InvitationStatus.CANCELLED);
        invitationRepository.save(invitation);
    }

}
