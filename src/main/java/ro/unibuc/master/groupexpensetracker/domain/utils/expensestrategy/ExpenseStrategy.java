package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy;

import com.tunyk.currencyconverter.api.CurrencyConverterException;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;

public interface ExpenseStrategy {

    void calculate(Expense expense) throws CurrencyConverterException;
}
