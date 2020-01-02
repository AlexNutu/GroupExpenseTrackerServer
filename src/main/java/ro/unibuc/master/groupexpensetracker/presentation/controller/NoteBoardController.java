package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.master.groupexpensetracker.domain.service.NoteBoardService;

@RestController
@RequestMapping("${noteboard.url}")
public class NoteBoardController {

    private final NoteBoardService noteBoardService;

    public NoteBoardController(NoteBoardService noteBoardService) {
        this.noteBoardService = noteBoardService;
    }
}
