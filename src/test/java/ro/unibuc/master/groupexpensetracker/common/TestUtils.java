package ro.unibuc.master.groupexpensetracker.common;

import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;

import java.util.Arrays;
import java.util.List;

public class TestUtils {

    public static UserProfile getMockedUserProfile1() {
        return new UserProfile("Michael");
    }

    public static UserProfile getMockedUserProfile2() {
        return new UserProfile("Robert");
    }

    public static Trip getMockedTrip() {
        List<UserProfile> members = Arrays.asList(getMockedUserProfile1(), getMockedUserProfile2());
        return new Trip("Oslo trip", members);
    }

    public static Expense getMockedExpense() {
        Trip trip = getMockedTrip();
        UserProfile userProfile = getMockedUserProfile1();
        return new Expense("Collect Expense", "Ported Boxes 2017", 23, "USD", 62.24F, userProfile, trip);
    }
}
