package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy;

import ro.unibuc.master.groupexpensetracker.data.expense.Expense;

public class CollectExpense implements ExpenseStrategy {
    @Override
    public void calculate(Expense expense) {
        //TODO: write implementation for collect expense
        System.out.println("Collect expense");
    }
}
