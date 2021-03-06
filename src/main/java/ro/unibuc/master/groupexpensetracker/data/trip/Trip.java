package ro.unibuc.master.groupexpensetracker.data.trip;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.AbstractAuditingEntity;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.presentation.dto.TripDTO;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Trip extends AbstractAuditingEntity {
    private String name;
    private String destination;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "trip_user",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<UserProfile> members;

    public static TripDTO toDto(Trip trip) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());
        tripDTO.setName(trip.getName());
        tripDTO.setDestination(trip.getDestination());
        tripDTO.setStartDate(trip.getStartDate().toString());
        tripDTO.setEndDate(trip.getEndDate().toString());
        List<UserDTO> members = trip.getMembers().stream()
                .map(UserProfile::toDto)
                .collect(Collectors.toList());
        tripDTO.setMembers(members);
        return tripDTO;
    }
}
