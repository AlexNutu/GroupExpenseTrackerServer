package ro.unibuc.master.groupexpensetracker.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.common.utils.EntitySpecification;
import ro.unibuc.master.groupexpensetracker.common.utils.EntityUtils;
import ro.unibuc.master.groupexpensetracker.common.utils.SearchCriteria;
import ro.unibuc.master.groupexpensetracker.data.note.Note;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.repository.NoteRepository;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;
import ro.unibuc.master.groupexpensetracker.presentation.dto.NoteDTO;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    private final TripService tripService;

    private final UserProfileService userProfileService;

    public NoteService(NoteRepository noteRepository, TripService tripService, UserProfileService userProfileService) {
        this.noteRepository = noteRepository;
        this.tripService = tripService;
        this.userProfileService = userProfileService;
    }

    public ResponseEntity addNote(Note note) {
        Trip trip = tripService.getTrip(note.getTrip().getId());
        UserProfile userProfile = userProfileService.getById(note.getUser().getId());
        if (trip == null) {
            throw new EntityNotFoundException("Could not find trip by id");
        }
        if (userProfile == null) {
            throw new EntityNotFoundException("Could not find user by id");
        }
        note.setApproved(false);
        note.setTrip(trip);
        note.setUser(userProfile);
        noteRepository.save(note);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateNote(Note note, long noteId) {
        Note noteDB = noteRepository.findById(noteId).orElseThrow(() -> new EntityNotFoundException("Could not find note by id"));
        noteDB.setMessage(note.getMessage());
        noteDB.setApproved(note.getApproved());
        noteRepository.save(noteDB);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity removeNote(long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new EntityNotFoundException("Could not find note by id"));
        noteRepository.delete(note);
        return ResponseEntity.ok().build();
    }

    public Page<NoteDTO> findAll(Sort.Direction sortingDirection, String orderBy, final String search, final Integer offset, final Integer size) {
        final List<SearchCriteria> searchCriteriaList = EntityUtils.generateSearchCriteria(search);
        final Specification<Note> spec = new EntitySpecification<>(searchCriteriaList);

        if (sortingDirection == null && orderBy == null) {
            orderBy = "createDate";
            sortingDirection = Sort.Direction.DESC;
        }

        if (size != null) {
            return noteRepository.findAll(spec,
                    EntityUtils.getPageRequest(sortingDirection, orderBy, offset, size))
                    .map(Note::toDto);
        } else {
            return new PageImpl<>(noteRepository.findAll(spec)).map(Note::toDto);
        }
    }
}
