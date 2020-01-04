package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.expense;

import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import ro.unibuc.master.groupexpensetracker.common.notification.NotificationTemplate;
import ro.unibuc.master.groupexpensetracker.common.utils.ExpenseUtils;
import ro.unibuc.master.groupexpensetracker.common.utils.StringUtils;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;
import ro.unibuc.master.groupexpensetracker.domain.service.NotificationService;
import ro.unibuc.master.groupexpensetracker.domain.service.TripService;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.ExpenseStrategy;
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
        float total = ExpenseUtils.calculateTotalExpenses(userExpenses, currency);
        if (total >= initialExpense.getSum()) {
            throw new IllegalExpenseException("The sum for " + expense.getProduct() + "has been already paid.");
        }

        expenseRepository.save(expense);

        userExpenses.add(expense);
        total = ExpenseUtils.calculateTotalExpenses(userExpenses, currency);
        float percent = total / initialExpense.getSum();
        String notificationType = percent < initialExpense.getPercent() ?
                NotificationTemplate.COLLECT_SUM : NotificationTemplate.COLLECT_SUM_SUCCESSFULLY;
        notificationService.saveCollectExpensiveNotification(expense, String.valueOf(percent), notificationType, initialExpense.getUserProfile());
    }
}
