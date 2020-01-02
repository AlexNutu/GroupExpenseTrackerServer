package ro.unibuc.master.groupexpensetracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.master.groupexpensetracker.presentation.dto.NotificationDTO;

import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/trackerApi")
public class GroupExpenseTrackerController {

    @RequestMapping(value = "/notification")
    public ResponseEntity<?> getNotification() {
        NotificationDTO n = new NotificationDTO(
                "Notification 1",
                "This is a notification!",
                new Date().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime());

        return new ResponseEntity<>(n, HttpStatus.OK);
    }

}
