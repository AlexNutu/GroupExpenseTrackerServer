package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.note.Note;
import ro.unibuc.master.groupexpensetracker.domain.service.NoteService;
import ro.unibuc.master.groupexpensetracker.presentation.dto.NoteDTO;
import ro.unibuc.master.groupexpensetracker.presentation.dto.TripDTO;

import java.util.List;

@RestController
@RequestMapping("${note.url}")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public Flux<NoteDTO> filterBy(@RequestParam(value = "search", required = false) final String search,
                                  @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                  @RequestParam(value = "orderBy", required = false) String orderBy,
                                  @RequestParam(value = "page", required = false) final Integer offset,
                                  @RequestParam(value = "size", required = false) final Integer size) {
        return Flux.fromIterable(noteService.findAll(direction, orderBy, search, offset, size));
    }

    @PostMapping
    public Mono<ResponseEntity> addNote(@RequestBody Note note) {
        return Mono.just(noteService.addNote(note));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity> updateNote(@RequestBody Note note, @PathVariable("id") Long noteId) {
        return Mono.just(noteService.updateNote(note, noteId));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity> removeNote(@PathVariable("id") Long noteId) {
        return Mono.just(noteService.removeNote(noteId));
    }
}
