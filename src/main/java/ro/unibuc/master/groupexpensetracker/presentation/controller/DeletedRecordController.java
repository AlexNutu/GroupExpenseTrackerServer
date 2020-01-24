package ro.unibuc.master.groupexpensetracker.presentation.controller;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.master.groupexpensetracker.data.deletedrecords.DeletedRecord;
import ro.unibuc.master.groupexpensetracker.domain.service.DeletedRecordService;
import ro.unibuc.master.groupexpensetracker.domain.service.ExpenseService;
import ro.unibuc.master.groupexpensetracker.domain.service.TripService;

import java.util.List;

@RestController
@RequestMapping("${deletedrecord.url}")
public class DeletedRecordController {

    private final DeletedRecordService deletedRecordService;

    public DeletedRecordController(DeletedRecordService deletedRecordService) {
        this.deletedRecordService = deletedRecordService;
    }
    @GetMapping
    public List<DeletedRecord> filterBy(@RequestParam(value = "search", required = false) final String search,
                                        @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                        @RequestParam(value = "orderBy", required = false) String orderBy,
                                        @RequestParam(value = "page", required = false) final Integer offset,
                                        @RequestParam(value = "size", required = false) final Integer size) {
        return deletedRecordService.findAll(direction, orderBy, search, offset, size).getContent();
    }
}
