package ro.unibuc.master.groupexpensetracker.data.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.AbstractAuditingEntity;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.presentation.dto.ExpenseDTO;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Expense extends AbstractAuditingEntity {
    private String expensiveType;
    private String product;
    private float sum;
    private String currency;
    private float percent;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private UserProfile user;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Trip trip;

    public static ExpenseDTO toDto(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setExpensiveType(expense.getExpensiveType());
        expenseDTO.setProduct(expense.getProduct());
        expenseDTO.setSum(expense.getSum());
        expenseDTO.setCurrency(expense.getCurrency());
        expenseDTO.setUser(UserProfile.toDto(expense.getUser()));
        return expenseDTO;
    }
}
