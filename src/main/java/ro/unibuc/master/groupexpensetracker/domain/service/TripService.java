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
import ro.unibuc.master.groupexpensetracker.common.utils.StringUtils;
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

    private final NotificationService notificationService;

    public TripService(TripRepository tripRepository, UserProfileService userProfileService,
                       NotificationService notificationService) {
        this.tripRepository = tripRepository;
        this.userProfileService = userProfileService;
        this.notificationService = notificationService;
    }

    public ResponseEntity addTrip(TripDTO tripDTO) {
        Trip trip = toEntity(tripDTO);
        tripRepository.save(trip);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateTrip(TripDTO tripDTO, Long tripId) {
        Trip tripDB = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find trip by id: " + tripId));
        tripDB.setName(tripDTO.getName());
        tripDB.setStartDate(StringUtils.convertStringToDate(tripDTO.getStartDate()).atTime(0, 0));
        tripDB.setEndDate(StringUtils.convertStringToDate(tripDTO.getStartDate()).atTime(0, 0));
        tripDB.setDestination(tripDTO.getDestination());

        tripRepository.save(tripDB);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity addNewMember(UserDTO userDTO, long tripId) {
        UserProfile userProfile = userProfileService.getById(userDTO.getId());
        Trip trip = getTrip(tripId);
        if (trip.getMembers().indexOf(userProfile) == -1) {
            trip.getMembers().add(userProfile);
            tripRepository.save(trip);

            for(UserProfile user : trip.getMembers()) {
                if (!user.equals(userProfile)) {
                    notificationService.saveAddMemberNotification(user, trip.getName(), userProfile);
                }
            }
        }
        return ResponseEntity.ok().build();
    }

    public TripDTO getTripById(Long id) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not find trip by id"));
        return Trip.toDto(trip);
    }

    public Trip getTrip(Long id) {
        return tripRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not find trip by id"));
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

    private Trip toEntity(TripDTO tripDTO) {
        Trip trip = new Trip();
        trip.setName(tripDTO.getName());
        trip.setDestination(tripDTO.getDestination());
        trip.setStartDate(StringUtils.convertStringToDate(tripDTO.getStartDate()).atTime(0, 0));
        trip.setEndDate(StringUtils.convertStringToDate(tripDTO.getEndDate()).atTime(0, 0));
        List<UserProfile> members = new ArrayList<>();
        for (UserDTO userDTO : tripDTO.getMembers()) {
            UserProfile userProfile = userProfileService.getById(userDTO.getId());
            members.add(userProfile);
        }
        trip.setMembers(members);
        return trip;
    }
}
