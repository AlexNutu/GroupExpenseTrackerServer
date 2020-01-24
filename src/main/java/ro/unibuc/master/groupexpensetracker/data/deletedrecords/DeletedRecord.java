package ro.unibuc.master.groupexpensetracker.data.deletedrecords;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.AbstractAuditingEntity;
import ro.unibuc.master.groupexpensetracker.data.note.Note;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.presentation.dto.DeletedRecordDTO;
import ro.unibuc.master.groupexpensetracker.presentation.dto.NoteDTO;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class DeletedRecord extends AbstractAuditingEntity {

    private Long recordId;
    private String tableName;


    public static DeletedRecordDTO toDto(DeletedRecord deletedRecord) {
        DeletedRecordDTO deletedRecordDTO = new DeletedRecordDTO();
        deletedRecordDTO.setCreateDate(deletedRecord.getCreateDate());
        deletedRecordDTO.setTableName(deletedRecord.getTableName());
        deletedRecordDTO.setRecordId(deletedRecord.getRecordId());
        return deletedRecordDTO;
    }
}
