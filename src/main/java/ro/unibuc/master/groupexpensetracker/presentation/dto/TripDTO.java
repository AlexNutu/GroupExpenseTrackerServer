package ro.unibuc.master.groupexpensetracker.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {
    private String name;
    private String destination;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<UserDTO> members;
}
