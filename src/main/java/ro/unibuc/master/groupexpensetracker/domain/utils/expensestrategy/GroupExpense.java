package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy;

import ro.unibuc.master.groupexpensetracker.data.expense.Expense;

public class GroupExpense implements ExpenseStrategy {
    @Override
    public void calculate(Expense expense) {
        //TODO: write implementation for group expense
        System.out.println("Group expence");
    }
}
