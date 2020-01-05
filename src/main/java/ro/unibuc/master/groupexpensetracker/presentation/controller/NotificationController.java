package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.master.groupexpensetracker.data.notification.Notification;
import ro.unibuc.master.groupexpensetracker.domain.service.NotificationService;

@RestController
@RequestMapping("${notification.url}")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public Page<Notification> filterBy(@RequestParam(value = "search", required = false) final String search,
                                       @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                       @RequestParam(value = "orderBy", required = false) String orderBy,
                                       @RequestParam(value = "page", required = false) final Integer offset,
                                       @RequestParam(value = "size", required = false) final Integer size) {
        return notificationService.findAll(direction, orderBy, search, offset, size);
    }
}
