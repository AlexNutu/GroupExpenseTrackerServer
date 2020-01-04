package ro.unibuc.master.groupexpensetracker.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.unibuc.master.groupexpensetracker.common.TestUtils;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.presentation.dto.NotificationDTO;

@SpringBootTest
@RunWith(SpringRunner.class)
class NotificationServiceTest {

    @Autowired
    NotificationService notificationService;

    @Test
    void testSendAddMemberNotification() {
        UserProfile newMember = TestUtils.getMockedUserProfile1();
        UserProfile currentMember = TestUtils.getMockedUserProfile2();
        Trip trip = TestUtils.getMockedTrip();

        NotificationDTO notificationDTO = notificationService.sendAddMemberNotification(newMember, trip.getName(), currentMember);
        System.out.println(notificationDTO);
    }
}