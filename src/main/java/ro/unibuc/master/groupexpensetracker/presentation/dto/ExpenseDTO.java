package ro.unibuc.master.groupexpensetracker.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {
    private String expensiveType;
    private String product;
    private float sum;
    private String currency;
    private UserDTO user;
}
