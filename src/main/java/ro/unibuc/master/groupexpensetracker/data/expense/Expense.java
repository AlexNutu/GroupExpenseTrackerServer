package ro.unibuc.master.groupexpensetracker.data.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    private String expensiveType;
    private String product;
    private float sum;
    private String currency;
    private float percent;
    private UserProfile userProfile;
    private Trip trip;
}
