package com.homeroha.repository;

import com.homeroha.model.UserInvitation;
import com.homeroha.model.Home;
import com.homeroha.dto.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserInvitationRepository extends JpaRepository<UserInvitation, UUID> {

    List<UserInvitation> findAllByEmailAndStatus(String email, InvitationStatus status);

    Optional<UserInvitation> findByIdAndEmail(UUID id, String email);

    boolean existsByEmailAndHomeAndStatus(String email, Home home, InvitationStatus status);
}
