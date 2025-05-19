package com.homeroha.service;

import com.homeroha.dto.NoteRequestDTO;
import com.homeroha.dto.NoteResponseDTO;

import java.util.List;

public interface NoteService {
    NoteResponseDTO addNote(String userEmail, NoteRequestDTO request);
    List<NoteResponseDTO> getNotes(String userEmail);
    NoteResponseDTO updateNote(Long id, String userEmail, NoteRequestDTO request);
    void deleteNote(Long id, String userEmail);
}
