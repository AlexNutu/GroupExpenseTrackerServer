package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.domain.service.TripService;
import ro.unibuc.master.groupexpensetracker.presentation.dto.TripDTO;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

import java.util.List;

@RestController
@RequestMapping("${trip.url}")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public Page<Trip> filterBy(@RequestParam(value = "search", required = false) final String search,
                               @RequestParam(value = "direction", required = false) Sort.Direction direction,
                               @RequestParam(value = "orderBy", required = false) String orderBy,
                               @RequestParam(value = "page", required = false) final Integer offset,
                               @RequestParam(value = "size", required = false) final Integer size) {
        return tripService.findAll(direction, orderBy, search, offset, size);
    }

    @PostMapping(value = "/{id}/member")
    public ResponseEntity addNewMember(@RequestBody UserDTO userDTO, @PathVariable("id") Long tripId) {
        return tripService.addNewMember(userDTO, tripId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getTripDetails(@PathVariable("id") Long tripId) {
        return ResponseEntity.ok().body(tripService.getTripById(tripId));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateTrip(@RequestBody TripDTO tripDTO, @PathVariable("id") Long tripId) {
        return tripService.updateTrip(tripDTO, tripId);
    }

    @PostMapping
    public ResponseEntity addTrip(@RequestBody TripDTO tripDTO) {
        return tripService.addTrip(tripDTO);
    }
}