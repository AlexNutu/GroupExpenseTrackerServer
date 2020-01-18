package ro.unibuc.master.groupexpensetracker.data.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.AbstractAuditingEntity;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.presentation.dto.NoteDTO;

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
    private UserProfile user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Trip trip;

    public static NoteDTO toDto(Note note) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setCreateDate(note.getCreateDate());
        noteDTO.setMessage(note.getMessage());
        noteDTO.setUser(UserProfile.toDto(note.getUser()));
        return noteDTO;
    }
}
