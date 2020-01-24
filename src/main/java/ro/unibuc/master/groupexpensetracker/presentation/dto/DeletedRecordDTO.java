package ro.unibuc.master.groupexpensetracker.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletedRecordDTO {
    private String tableName;
    private Long recordId;
    private LocalDateTime createDate;

}
