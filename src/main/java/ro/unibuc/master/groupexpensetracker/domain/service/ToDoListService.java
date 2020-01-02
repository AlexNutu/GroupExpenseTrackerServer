package ro.unibuc.master.groupexpensetracker.domain.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.data.noteboard.ToDoList;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.repository.ToDoListRepository;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;

@Service
public class ToDoListService {

    private final ToDoListRepository toDoListRepository;

    private final TripService tripService;

    private final UserProfileService userProfileService;

    public ToDoListService(ToDoListRepository toDoListRepository, TripService tripService, UserProfileService userProfileService) {
        this.toDoListRepository = toDoListRepository;
        this.tripService = tripService;
        this.userProfileService = userProfileService;
    }

    public ResponseEntity addToDoList(ToDoList toDoList) {
        Trip trip = tripService.getTrip(toDoList.getId());
        toDoList.setTrip(trip);
        toDoList.setFinished(true);
        toDoList.setUserProfile(null);
        toDoListRepository.save(toDoList);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateToDoList(ToDoList toDoList) {
        Trip trip = tripService.getTrip(toDoList.getTrip().getId());
        ToDoList toDoListDB = toDoListRepository.findById(toDoList.getId())
                .orElseThrow(() -> new EntityNotFoundException("Could not retrieve ToDoList by Id"));
        if (!toDoListDB.isFinished() && toDoListDB.getUserProfile().getId().equals(toDoList.getUserProfile().getId())) {
            toDoListDB.setTrip(trip);
            toDoListDB.setDescription(toDoList.getDescription());
            toDoListDB.setFinished(true);
            toDoListDB.setUserProfile(null);
            toDoListRepository.save(toDoListDB);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Could not edit ToDoList now");
    }

    public ToDoList getByTripId(long tripId) {
        return toDoListRepository.findByTripId(tripId);
    }

    public ResponseEntity getWritingAccess(ToDoList noteBoard) {
        ToDoList toDoList = toDoListRepository.findById(noteBoard.getId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find ToDoList by id"));
        if (toDoList.isFinished()) {
            UserProfile userProfile = userProfileService.getById(noteBoard.getUserProfile().getId());
            toDoList.setUserProfile(userProfile);
            toDoList.setFinished(false);
            toDoListRepository.save(toDoList);
            return ResponseEntity.ok().body(toDoList);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Could not write in ToDoList now");
        }
    }

    public ResponseEntity removeAccess(ToDoList noteBoard) {
        ToDoList toDoList = toDoListRepository.findById(noteBoard.getId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find ToDoList by id"));
        toDoList.setFinished(true);
        toDoList.setUserProfile(null);
        toDoListRepository.save(toDoList);
        return ResponseEntity.ok().build();
    }
}
