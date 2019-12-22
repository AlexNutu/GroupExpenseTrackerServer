package ro.unibuc.master.groupexpensetracker.data.noteboardlock;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.AbstractAuditingEntity;
import ro.unibuc.master.groupexpensetracker.data.noteboard.NoteBoard;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class NoteBoardLock extends AbstractAuditingEntity {
    private boolean locked;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UserProfile userProfile;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private NoteBoard noteBoard;
}
