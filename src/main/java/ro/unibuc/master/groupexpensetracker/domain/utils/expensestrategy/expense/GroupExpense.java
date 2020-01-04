package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.expense;

import com.tunyk.currencyconverter.BankUaCom;
import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverter;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import ro.unibuc.master.groupexpensetracker.common.utils.StringUtils;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;
import ro.unibuc.master.groupexpensetracker.domain.service.NotificationService;
import ro.unibuc.master.groupexpensetracker.domain.service.TripService;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.ExpenseStrategy;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;
import ro.unibuc.master.groupexpensetracker.exception.IllegalExpenseException;

import java.util.List;

public class GroupExpense implements ExpenseStrategy {

    private final ExpenseRepository expenseRepository;

    private final NotificationService notificationService;

    private final TripService tripService;

    public GroupExpense(ExpenseRepository expenseRepository, NotificationService notificationService, TripService tripService) {
        this.expenseRepository = expenseRepository;
        this.notificationService = notificationService;
        this.tripService = tripService;
    }

    @Override
    public void calculate(Expense expense) throws CurrencyConverterException {
        if (!expense.getCurrency().equals("RON")) {
            Currency.fromString(expense.getCurrency());
        }
        Expense initialExpense = expenseRepository.findByProductAndExpensiveTypeAndTripId(expense.getProduct(), StringUtils.INITIAL_GROUP_EXPENSE, expense.getTrip().getId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find initial group expense for " + expense.getProduct()));
        List<Expense> userExpenses = expenseRepository.findByProductAndExpensiveTypeAndUserProfileIdAndTripId(expense.getProduct(), StringUtils.GROUP_EXPENSE,
                expense.getUserProfile().getId(), expense.getTrip().getId());

        Currency currency;
        if (!initialExpense.getCurrency().equals("RON")) {
            currency = Currency.fromString(initialExpense.getCurrency());
        } else {
            currency = Currency.MDL;
        }
        float total = calculateTotalExpenses(userExpenses, currency);
        if (total >= initialExpense.getSum()) {
            throw new IllegalExpenseException("The sum for " + expense.getProduct() + "has been already paid.");
        }

        expenseRepository.save(expense);
        notificationService.saveExpenseNotification(expense, initialExpense.getUserProfile());
    }

    private float calculateTotalExpenses(List<Expense> expenses, Currency currency) throws CurrencyConverterException {
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
