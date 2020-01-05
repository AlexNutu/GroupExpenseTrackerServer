package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.context;

import com.tunyk.currencyconverter.api.CurrencyConverterException;
import lombok.AllArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.ExpenseStrategy;

@AllArgsConstructor
public class ContextExpense {
    private ExpenseStrategy strategy;

    public void executeCalculate(Expense expense) throws CurrencyConverterException {
        strategy.calculate(expense);
    }
}
