package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy;

import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;

public class GroupExpense implements ExpenseStrategy {

    private ExpenseRepository expenseRepository;

    public GroupExpense(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public void calculate(Expense expense) {
        expenseRepository.save(expense);
        System.out.println("Group expense");
    }
}
