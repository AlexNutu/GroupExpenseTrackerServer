package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.master.groupexpensetracker.data.noteboard.ToDoList;
import ro.unibuc.master.groupexpensetracker.domain.service.ToDoListService;

@RestController
@RequestMapping("${noteboard.url}")
public class ToDoListController {

    private final ToDoListService toDoListService;

    public ToDoListController(ToDoListService toDoListService) {
        this.toDoListService = toDoListService;
    }

    @PostMapping
    public ResponseEntity addToDoList(@RequestBody ToDoList toDoList) {
        return toDoListService.addToDoList(toDoList);
    }

    @PutMapping
    public ResponseEntity updateToDoList(@RequestBody ToDoList toDoList) {
        return toDoListService.updateToDoList(toDoList);
    }

    @GetMapping(value = "/trip/{tripId}")
    public ResponseEntity getToDoList(@PathVariable("tripId") Long tripId) {
        return ResponseEntity.ok().body(toDoListService.getByTripId(tripId));
    }

    @PutMapping(value = "/access")
    public ResponseEntity getWritingAccess(@RequestBody ToDoList toDoList) {
        return toDoListService.getWritingAccess(toDoList);
    }

    @GetMapping(value = "/remove-access")
    public ResponseEntity removeAccess(@RequestBody ToDoList toDoList) {
        return toDoListService.removeAccess(toDoList);
    }
}
