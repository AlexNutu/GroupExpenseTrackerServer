package ro.unibuc.master.groupexpensetracker.data.userprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ro.unibuc.master.groupexpensetracker.data.AbstractAuditingEntity;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class UserProfile extends AbstractAuditingEntity {
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "members", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Trip> tripList;

    public static UserDTO toDto(UserProfile userProfile) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userProfile.getId());
        userDTO.setFirstName(userProfile.getFirstName());
        userDTO.setLastName(userProfile.getLastName());
        userDTO.setEmail(userProfile.getEmail());
        return userDTO;
    }
}
