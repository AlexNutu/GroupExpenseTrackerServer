package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy;

import ro.unibuc.master.groupexpensetracker.data.expense.Expense;

public class SimpleExpense implements ExpenseStrategy {
    @Override
    public void calculate(Expense expense) {
        //TODO: write implementation for simple expense
        System.out.println("Simple expence");
    }
}
