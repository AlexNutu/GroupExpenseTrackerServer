package ro.unibuc.master.groupexpensetracker.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.common.utils.StringUtils;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.CollectExpense;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.ContextExpense;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.GroupExpense;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.SimpleExpense;
import ro.unibuc.master.groupexpensetracker.exception.IllegalExpensiveException;

@Slf4j
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void processExpense(Expense expense) {
        ContextExpense contextExpense = null;
        switch (expense.getExpensiveType()) {
            case StringUtils.SIMPLE_EXPENSE:
                contextExpense = new ContextExpense(new SimpleExpense());
                break;
            case StringUtils.GROUP_EXPENSE:
                contextExpense = new ContextExpense(new GroupExpense());
                break;
            case StringUtils.COLLECT_EXPENSE:
                contextExpense = new ContextExpense(new CollectExpense());
                break;
            default:
                log.error("Could not found this type of expensive: " + expense.getExpensiveType());
                throw new IllegalExpensiveException("Could not found this type of expensive: " + expense.getExpensiveType());
        }
        contextExpense.executeCalculate(expense);
    }
}
