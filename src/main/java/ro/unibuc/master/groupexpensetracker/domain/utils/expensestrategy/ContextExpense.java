package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy;

import com.tunyk.currencyconverter.api.CurrencyConverterException;
import lombok.AllArgsConstructor;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;

@AllArgsConstructor
public class ContextExpense {
    private ExpenseStrategy strategy;

    public void executeCalculate(Expense expense) throws CurrencyConverterException {
        strategy.calculate(expense);
    }
}
