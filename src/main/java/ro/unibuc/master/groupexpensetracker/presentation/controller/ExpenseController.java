package ro.unibuc.master.groupexpensetracker.presentation.controller;

import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.domain.service.ExpenseService;
import ro.unibuc.master.groupexpensetracker.presentation.dto.ExpenseDTO;

@RestController
@RequestMapping("${expense.url}")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public Flux<ExpenseDTO> filterBy(@RequestParam(value = "search", required = false) final String search,
                                     @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                     @RequestParam(value = "orderBy", required = false) String orderBy,
                                     @RequestParam(value = "page", required = false) final Integer offset,
                                     @RequestParam(value = "size", required = false) final Integer size) {
        return Flux.fromIterable(expenseService.findAll(direction, orderBy, search, offset, size).getContent());
    }

    @PostMapping
    public Mono<ResponseEntity> addExpense(@RequestBody Expense expense) throws CurrencyConverterException {
        return Mono.just(expenseService.processExpense(expense));
    }

    @GetMapping("/report/trip/{id}")
    public Mono<ResponseEntity> getReport(@RequestParam(value = "product", required = false) String product,
                                    @PathVariable("id") Long tripId) throws CurrencyConverterException {
        return Mono.just(ResponseEntity.ok().body(expenseService.getUnperformedExpenses(tripId, product)));
    }
}
