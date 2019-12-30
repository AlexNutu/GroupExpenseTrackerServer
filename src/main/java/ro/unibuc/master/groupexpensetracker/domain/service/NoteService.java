package ro.unibuc.master.groupexpensetracker.domain.service;

import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.domain.repository.NoteRepository;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }
}
