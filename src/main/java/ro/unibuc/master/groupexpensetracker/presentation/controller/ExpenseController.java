package ro.unibuc.master.groupexpensetracker.presentation.controller;

import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.domain.service.ExpenseService;
import ro.unibuc.master.groupexpensetracker.presentation.dto.ExpenseDTO;

import java.util.List;

@RestController
@RequestMapping("${expense.url}")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<ExpenseDTO> filterBy(@RequestParam(value = "search", required = false) final String search,
                                     @RequestParam(value = "direction", required = false) Sort.Direction direction,
                                     @RequestParam(value = "orderBy", required = false) String orderBy,
                                     @RequestParam(value = "page", required = false) final Integer offset,
                                     @RequestParam(value = "size", required = false) final Integer size) {
        return expenseService.findAll(direction, orderBy, search, offset, size);
    }

    @PostMapping
    public ResponseEntity addExpense(@RequestBody Expense expense) throws CurrencyConverterException {
        return expenseService.processExpense(expense);
    }

    @GetMapping("/report/trip/{id}")
    public ResponseEntity getReport(@RequestParam(value = "product", required = false) String product,
                                    @PathVariable("id") Long tripId) throws CurrencyConverterException {
        return ResponseEntity.ok().body(expenseService.getUnperformedExpenses(tripId, product));
    }
}
