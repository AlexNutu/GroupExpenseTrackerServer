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
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.repository.TripRepository;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;
import ro.unibuc.master.groupexpensetracker.presentation.dto.TripDTO;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    private final UserProfileService userProfileService;

    public TripService(TripRepository tripRepository, UserProfileService userProfileService) {
        this.tripRepository = tripRepository;
        this.userProfileService = userProfileService;
    }

    public ResponseEntity addTrip(Trip trip) {
        tripRepository.save(trip);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateTrip(Trip trip, Long tripId) {
        Trip tripDB = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find trip by id: " + tripId));
        tripDB.setName(trip.getName());
        tripDB.setStartDate(trip.getStartDate());
        tripDB.setDestination(trip.getDestination());

        tripRepository.save(trip);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity addNewMembers(List<UserDTO> userDTOS) {
        List<UserProfile> userProfileList = new ArrayList<>();
        for (UserDTO userDTO : userDTOS) {
            UserProfile userProfile = userProfileService.getById(userDTO.getId());
            userProfileList.add(userProfile);
        }
        userProfileService.saveAll(userProfileList);
        return ResponseEntity.ok().build();
    }

    public TripDTO getTripById(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not find trip by id"));
        return Trip.toDto(trip);
    }

    public Page<Trip> findAll(Sort.Direction sortingDirection, String orderBy, final String search, final Integer offset, final Integer size) {
        final List<SearchCriteria> searchCriteriaList = EntityUtils.generateSearchCriteria(search);
        final Specification<Trip> spec = new EntitySpecification<>(searchCriteriaList);

        if (sortingDirection == null && orderBy == null) {
            orderBy = "createDate";
            sortingDirection = Sort.Direction.DESC;
        }

        if (size != null) {
            return tripRepository.findAll(spec,
                    EntityUtils.getPageRequest(sortingDirection, orderBy, offset, size));
        } else {
            return new PageImpl<>(tripRepository.findAll(spec));
        }
    }
}
