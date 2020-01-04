package ro.unibuc.master.groupexpensetracker.data.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.AbstractAuditingEntity;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends AbstractAuditingEntity {
    private String title;

    private String message;

    private boolean sent;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UserProfile user;
}
