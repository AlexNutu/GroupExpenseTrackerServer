package ro.unibuc.master.groupexpensetracker.domain.service;

import com.tunyk.currencyconverter.api.CurrencyConverterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.common.utils.EntitySpecification;
import ro.unibuc.master.groupexpensetracker.common.utils.EntityUtils;
import ro.unibuc.master.groupexpensetracker.common.utils.SearchCriteria;
import ro.unibuc.master.groupexpensetracker.common.utils.StringUtils;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.CollectExpense;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.ContextExpense;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.GroupExpense;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.SimpleExpense;
import ro.unibuc.master.groupexpensetracker.exception.IllegalExpenseException;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final NotificationService notificationService;

    private final TripService tripService;

    public ExpenseService(ExpenseRepository expenseRepository, NotificationService notificationService, TripService tripService) {
        this.expenseRepository = expenseRepository;
        this.notificationService = notificationService;
        this.tripService = tripService;
    }

    public ResponseEntity processExpense(Expense expense) throws CurrencyConverterException {
        ContextExpense contextExpense = setContextExpenseByExpenseType(expense);
        contextExpense.executeCalculate(expense);
        return ResponseEntity.ok().build();
    }

    private ContextExpense setContextExpenseByExpenseType(Expense expense) {
        ContextExpense contextExpense;
        switch (expense.getExpensiveType()) {
            case StringUtils.SIMPLE_EXPENSE:
                contextExpense = new ContextExpense(new SimpleExpense(expenseRepository, notificationService, tripService));
                break;
            case StringUtils.GROUP_EXPENSE:
                contextExpense = new ContextExpense(new GroupExpense(expenseRepository, notificationService, tripService));
                break;
            case StringUtils.INITIAL_GROUP_EXPENSE:
                contextExpense = new ContextExpense(new GroupExpense(expenseRepository, notificationService, tripService));
                break;
            case StringUtils.COLLECT_EXPENSE:
                contextExpense = new ContextExpense(new CollectExpense(expenseRepository, notificationService, tripService));
                break;
            case StringUtils.INITIAL_COLLECT_EXPENSE:
                contextExpense = new ContextExpense(new CollectExpense(expenseRepository, notificationService, tripService));
                break;
            case StringUtils.FINAL_COLLECT_EXPENSE:
                contextExpense = new ContextExpense(new CollectExpense(expenseRepository, notificationService, tripService));
                break;
            default:
                throw new IllegalExpenseException("Could not found this type of expensive: " + expense.getExpensiveType());
        }
        return contextExpense;
    }

    public Page<Expense> findAll(Sort.Direction sortingDirection, String orderBy, final String search, final Integer offset, final Integer size) {
        final List<SearchCriteria> searchCriteriaList = EntityUtils.generateSearchCriteria(search);
        final Specification<Expense> spec = new EntitySpecification<>(searchCriteriaList);

        if (sortingDirection == null && orderBy == null) {
            orderBy = "createDate";
            sortingDirection = Sort.Direction.DESC;
        }

        if (size != null) {
            return expenseRepository.findAll(spec,
                    EntityUtils.getPageRequest(sortingDirection, orderBy, offset, size));
        } else {
            return new PageImpl<>(expenseRepository.findAll(spec));
        }
    }
}
