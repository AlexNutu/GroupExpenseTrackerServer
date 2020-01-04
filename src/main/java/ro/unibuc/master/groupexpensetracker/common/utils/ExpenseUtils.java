package ro.unibuc.master.groupexpensetracker.common.utils;

import com.tunyk.currencyconverter.BankUaCom;
import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverter;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;

import java.util.List;

public class ExpenseUtils {

    public static float calculateTotalExpenses(List<Expense> expenses, Currency currency) throws CurrencyConverterException {
        float total = 0;
        for (Expense expense : expenses) {
            Currency expenseCurrency;
            float exchangeRate = 1;
            if (!expense.getCurrency().equals("RON")) {
                expenseCurrency = Currency.fromString(expense.getCurrency());
            } else {
                expenseCurrency = Currency.MDL;
                exchangeRate = 4F;
            }

            if (currency.equals(Currency.MDL)) {
                exchangeRate = exchangeRate * 0.25F;
            }

            CurrencyConverter currencyConverter = new BankUaCom(expenseCurrency, currency);
            total += currencyConverter.convertCurrency(expense.getSum() * exchangeRate);
        }
        return total;
    }
}
