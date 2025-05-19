package com.homeroha.service.impl;

import com.homeroha.dto.SharedContactRequestDTO;
import com.homeroha.dto.SharedContactResponseDTO;
import com.homeroha.model.Home;
import com.homeroha.model.SharedContact;
import com.homeroha.model.User;
import com.homeroha.repository.HomeRepository;
import com.homeroha.repository.SharedContactRepository;
import com.homeroha.repository.UserRepository;
import com.homeroha.service.SharedContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedContactServiceImpl implements SharedContactService {

    private final SharedContactRepository sharedContactRepository;
    private final UserRepository userRepository;
    private final HomeRepository homeRepository;

    @Override
    public SharedContactResponseDTO addContact(String email, SharedContactRequestDTO request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Home home = homeRepository.findById(request.getHomeId())
                .orElseThrow(() -> new RuntimeException("Home not found"));

        SharedContact contact = new SharedContact();
        contact.setName(request.getName());
        contact.setPhoneNumbers(request.getPhoneNumbers());
        contact.setEmail(request.getEmail());
        contact.setCategory(request.getCategory());
        contact.setTags(request.getTags());
        contact.setNotes(request.getNotes());
        contact.setLocation(request.getLocation());
        contact.setIsVerified(request.getIsVerified());
        contact.setAddedBy(user);
        contact.setCreatedAt(LocalDateTime.now());
        contact.setHome(home);

        sharedContactRepository.save(contact);

        return mapToDTO(contact);
    }

    @Override
    public Page<SharedContactResponseDTO> listContacts(String email, Long homeId, int page, int size, String search) {
        Page<SharedContact> contacts = sharedContactRepository.findByHomeId(homeId, PageRequest.of(page, size));
        return new PageImpl<>(contacts.stream().map(this::mapToDTO).collect(Collectors.toList()));
    }

    @Override
    public SharedContactResponseDTO getContact(Long contactId) {
        return sharedContactRepository.findById(contactId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Shared contact not found"));
    }

    @Override
    public SharedContactResponseDTO updateContact(Long contactId, SharedContactRequestDTO request) {
        SharedContact contact = sharedContactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setName(request.getName());
        contact.setPhoneNumbers(request.getPhoneNumbers());
        contact.setEmail(request.getEmail());
        contact.setCategory(request.getCategory());
        contact.setTags(request.getTags());
        contact.setNotes(request.getNotes());
        contact.setLocation(request.getLocation());
        contact.setIsVerified(request.getIsVerified());
        contact.setUpdatedAt(LocalDateTime.now());

        sharedContactRepository.save(contact);
        return mapToDTO(contact);
    }

    @Override
    public void deleteContact(Long contactId) {
        SharedContact contact = sharedContactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        sharedContactRepository.delete(contact);
    }

    private SharedContactResponseDTO mapToDTO(SharedContact contact) {
        return SharedContactResponseDTO.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phoneNumbers(contact.getPhoneNumbers())
                .email(contact.getEmail())
                .category(contact.getCategory())
                .tags(contact.getTags())
                .notes(contact.getNotes())
                .location(contact.getLocation())
                .isVerified(contact.getIsVerified())
                .addedBy(contact.getAddedBy() != null ? contact.getAddedBy().getName() : null)
                .updatedBy(contact.getUpdatedBy() != null ? contact.getUpdatedBy().getName() : null)
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
}
