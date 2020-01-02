package ro.unibuc.master.groupexpensetracker.domain.service;

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
import ro.unibuc.master.groupexpensetracker.exception.IllegalExpensiveException;

import java.util.List;

@Slf4j
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ResponseEntity processExpense(Expense expense) {
        ContextExpense contextExpense = setContextExpenseByExpenseType(expense);
        contextExpense.executeCalculate(expense);
        return ResponseEntity.ok().build();
    }

    private ContextExpense setContextExpenseByExpenseType(Expense expense) {
        ContextExpense contextExpense = null;
        switch (expense.getExpensiveType()) {
            case StringUtils.SIMPLE_EXPENSE:
                contextExpense = new ContextExpense(new SimpleExpense(expenseRepository));
                break;
            case StringUtils.GROUP_EXPENSE:
                contextExpense = new ContextExpense(new GroupExpense(expenseRepository));
                break;
            case StringUtils.COLLECT_EXPENSE:
                contextExpense = new ContextExpense(new CollectExpense(expenseRepository));
                break;
            default:
                throw new IllegalExpensiveException("Could not found this type of expensive: " + expense.getExpensiveType());
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
