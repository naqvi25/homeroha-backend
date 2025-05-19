package com.homeroha.service;

import com.homeroha.dto.InvitationRequestDTO;
import com.homeroha.model.UserInvitation;

import java.util.List;
import java.util.UUID;

public interface InvitationService {
    void sendInvitation(String senderEmail, InvitationRequestDTO request);
    List<UserInvitation> getPendingInvitationsForUser(String email);
    void acceptInvitation(UUID inviteId, String userEmail);
    void declineInvitation(UUID inviteId, String email);
    void cancelInvitation(UUID inviteId, String senderEmail);
}
