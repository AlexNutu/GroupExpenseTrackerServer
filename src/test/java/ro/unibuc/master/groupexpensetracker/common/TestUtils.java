package ro.unibuc.master.groupexpensetracker.common;

import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;

import java.util.Arrays;
import java.util.List;

public class TestUtils {

    public static UserProfile getMockedUserProfile1() {
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("Thompson");
        userProfile.setLastName("Michael");
        return userProfile;
    }

    public static UserProfile getMockedUserProfile2() {
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("Covington");
        userProfile.setLastName("Robert");
        return userProfile;
    }

    public static Trip getMockedTrip() {
        List<UserProfile> members = Arrays.asList(getMockedUserProfile1(), getMockedUserProfile2());
        Trip trip = new Trip();
        trip.setName("Oslo trip");
        trip.setMembers(members);
        return trip;
    }

    public static Expense getMockedExpense() {
        Trip trip = getMockedTrip();
        UserProfile userProfile = getMockedUserProfile1();
        return new Expense("Collect Expense", "Ported Boxes 2017", 23, "USD", userProfile, trip);
    }
}
