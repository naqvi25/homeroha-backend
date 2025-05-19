package com.homeroha.service.impl;

import com.homeroha.dto.NoteRequestDTO;
import com.homeroha.dto.NoteResponseDTO;
import com.homeroha.exception.HomerohaException;
import com.homeroha.model.*;
import com.homeroha.repository.*;
import com.homeroha.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    @Override
    public NoteResponseDTO addNote(String userEmail, NoteRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        Home home = user.getActiveHome();
        if (home == null) throw new HomerohaException("No active home selected");

        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .reminderTime(request.getReminderTime())
                .createdBy(user)
                .home(home)
                .build();

        noteRepository.save(note);

        return buildResponse(note);
    }

    @Override
    public List<NoteResponseDTO> getNotes(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        Home home = user.getActiveHome();
        if (home == null) throw new HomerohaException("No active home selected");

        return noteRepository.findAllByHome(home).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    public NoteResponseDTO updateNote(Long id, String userEmail, NoteRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        Home home = user.getActiveHome();
        if (home == null) throw new HomerohaException("No active home selected");

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Note not found"));

        if (!note.getHome().getId().equals(home.getId())) {
            throw new HomerohaException("Unauthorized to update this note");
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setReminderTime(request.getReminderTime());

        noteRepository.save(note);
        return buildResponse(note);
    }

    @Override
    public void deleteNote(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new HomerohaException("User not found"));
        Home home = user.getActiveHome();
        if (home == null) throw new HomerohaException("No active home selected");

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new HomerohaException("Note not found"));

        if (!note.getHome().getId().equals(home.getId())) {
            throw new HomerohaException("Unauthorized to delete this note");
        }

        noteRepository.delete(note);
    }

    private NoteResponseDTO buildResponse(Note note) {
        return NoteResponseDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .reminderTime(note.getReminderTime())
                .createdBy(note.getCreatedBy().getName())
                .build();
    }
}
