package ro.unibuc.master.groupexpensetracker.data.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ro.unibuc.master.groupexpensetracker.data.AbstractAuditingEntity;
import ro.unibuc.master.groupexpensetracker.data.noteboard.NoteBoard;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Note extends AbstractAuditingEntity {
    private String message;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UserProfile userProfile;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private NoteBoard noteBoard;
}
