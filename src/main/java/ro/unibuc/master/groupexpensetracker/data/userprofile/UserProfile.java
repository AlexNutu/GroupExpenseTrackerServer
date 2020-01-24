package ro.unibuc.master.groupexpensetracker.data.userprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.DefaultValue;
import ro.unibuc.master.groupexpensetracker.data.AbstractAuditingEntity;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.presentation.dto.UserDTO;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class UserProfile extends AbstractAuditingEntity {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Boolean receiveNotifications;

    public UserProfile() {
    }

    public UserProfile(Long id, String firstName, String lastName, String email, String password, Boolean receiveNotif) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.receiveNotifications = receiveNotif;
    }

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
        userDTO.setReceiveNotifications(userProfile.getReceiveNotifications());
        return userDTO;
    }
}
