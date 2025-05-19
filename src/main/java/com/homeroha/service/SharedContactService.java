package com.homeroha.service;

import com.homeroha.dto.SharedContactRequestDTO;
import com.homeroha.dto.SharedContactResponseDTO;
import org.springframework.data.domain.Page;

public interface SharedContactService {
    SharedContactResponseDTO addContact(String email, SharedContactRequestDTO request);
    Page<SharedContactResponseDTO> listContacts(String email, Long homeId, int page, int size, String search);
    SharedContactResponseDTO getContact(Long contactId);
    SharedContactResponseDTO updateContact(Long contactId, SharedContactRequestDTO request);
    void deleteContact(Long contactId);
}