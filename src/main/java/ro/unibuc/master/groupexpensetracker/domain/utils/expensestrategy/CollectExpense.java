package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy;

import com.tunyk.currencyconverter.BankUaCom;
import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverter;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import ro.unibuc.master.groupexpensetracker.common.notification.NotificationTemplate;
import ro.unibuc.master.groupexpensetracker.common.utils.StringUtils;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;
import ro.unibuc.master.groupexpensetracker.domain.service.NotificationService;
import ro.unibuc.master.groupexpensetracker.domain.service.TripService;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;
import ro.unibuc.master.groupexpensetracker.exception.IllegalExpenseException;

import java.util.List;

public class CollectExpense implements ExpenseStrategy {

    private final ExpenseRepository expenseRepository;

    private final NotificationService notificationService;

    private final TripService tripService;

    public CollectExpense(ExpenseRepository expenseRepository, NotificationService notificationService, TripService tripService) {
        this.expenseRepository = expenseRepository;
        this.notificationService = notificationService;
        this.tripService = tripService;
    }

    @Override
    public void calculate(Expense expense) throws CurrencyConverterException {
        if (!expense.getCurrency().equals("RON")) {
            Currency.fromString(expense.getCurrency());
        }
        Trip trip = tripService.getTrip(expense.getTrip().getId());
        if (expense.getExpensiveType().equals(StringUtils.INITIAL_COLLECT_EXPENSE)) {
            expenseRepository.save(expense);
            notificationService.saveInitialCollectExpenseNotification(expense, trip.getMembers());
        }
        if (expense.getExpensiveType().equals(StringUtils.FINAL_COLLECT_EXPENSE)) {
            Expense initialExpense = expenseRepository.findByProductAndExpensiveTypeAndTripId(expense.getProduct(), StringUtils.INITIAL_COLLECT_EXPENSE, expense.getTrip().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Could not find initial collect expense for " + expense.getProduct()));
            if (!initialExpense.getUserProfile().equals(expense.getUserProfile())) {
                throw new IllegalExpenseException("Unauthorized to performe the expense");
            }

            List<Expense> expenses = expenseRepository.findByProductAndTripId(expense.getProduct(), expense.getTrip().getId());
            Currency currency;
            if (!initialExpense.getCurrency().equals("RON")) {
                currency = Currency.fromString(initialExpense.getCurrency());
            } else {
                currency = Currency.MDL;
            }
            float total = calculateTotalExpenses(expenses, currency);
            boolean checkExpense = total / initialExpense.getSum() < initialExpense.getPercent();
            if (checkExpense) {
                expenseRepository.save(expense);
                for (UserProfile userProfile : trip.getMembers()) {
                    if (!userProfile.equals(expense.getUserProfile())) {
                        notificationService.saveExpenseNotification(expense, userProfile);
                    }
                }
            }
        }
        if (expense.getExpensiveType().equals(StringUtils.COLLECT_EXPENSE)) {
            Expense initialExpense = expenseRepository.findByProductAndExpensiveTypeAndTripId(expense.getProduct(), StringUtils.INITIAL_COLLECT_EXPENSE, expense.getTrip().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Could not find initial collect expense for " + expense.getProduct()));
            List<Expense> userExpenses = expenseRepository.findByProductAndExpensiveTypeAndUserProfileIdAndTripId(expense.getProduct(), StringUtils.COLLECT_EXPENSE,
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

            userExpenses.add(expense);
            total = calculateTotalExpenses(userExpenses, currency);
            float percent = total / initialExpense.getSum();
            String notificationType = percent < initialExpense.getPercent() ?
                    NotificationTemplate.COLLECT_SUM : NotificationTemplate.COLLECT_SUM_SUCCESSFULLY;
            notificationService.saveCollectExpensiveNotification(expense, String.valueOf(percent), notificationType, initialExpense.getUserProfile());
        }
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
