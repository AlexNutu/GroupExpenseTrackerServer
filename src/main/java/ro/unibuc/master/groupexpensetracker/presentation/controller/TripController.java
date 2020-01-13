package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.domain.service.TripService;
import ro.unibuc.master.groupexpensetracker.presentation.dto.TripDTO;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

@RestController
@RequestMapping("${trip.url}")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public Flux<Trip> filterBy(@RequestParam(value = "search", required = false) final String search,
                               @RequestParam(value = "direction", required = false) Sort.Direction direction,
                               @RequestParam(value = "orderBy", required = false) String orderBy,
                               @RequestParam(value = "page", required = false) final Integer offset,
                               @RequestParam(value = "size", required = false) final Integer size) {
        return Flux.fromIterable(tripService.findAll(direction, orderBy, search, offset, size).getContent());
    }

    @PostMapping(value = "/{id}/member")
    public Mono<ResponseEntity> addNewMember(@RequestBody UserDTO userDTO, @PathVariable("id") Long tripId) {
        return Mono.just(tripService.addNewMember(userDTO, tripId));
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity> getTripDetails(@PathVariable("id") Long tripId) {
        return Mono.just(ResponseEntity.ok().body(tripService.getTripById(tripId)));
    }

    @PutMapping(value = "/{id}")
    public Mono<ResponseEntity> updateTrip(@RequestBody TripDTO tripDTO, @PathVariable("id") Long tripId) {
        return Mono.just(tripService.updateTrip(tripDTO, tripId));
    }

    @PostMapping
    public Mono<ResponseEntity> addTrip(@RequestBody TripDTO tripDTO) {
        return Mono.just(ResponseEntity.ok().body(tripService.addTrip(tripDTO)));
    }
}
