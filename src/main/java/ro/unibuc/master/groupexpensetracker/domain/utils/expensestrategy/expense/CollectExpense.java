package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.expense;

import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import ro.unibuc.master.groupexpensetracker.common.notification.NotificationTemplate;
import ro.unibuc.master.groupexpensetracker.common.utils.ExpenseUtils;
import ro.unibuc.master.groupexpensetracker.common.utils.StringUtils;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;
import ro.unibuc.master.groupexpensetracker.domain.service.NotificationService;
import ro.unibuc.master.groupexpensetracker.domain.service.TripService;
import ro.unibuc.master.groupexpensetracker.domain.service.UserProfileService;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.ExpenseStrategy;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;
import ro.unibuc.master.groupexpensetracker.exception.IllegalExpenseException;

import java.util.List;

public class CollectExpense implements ExpenseStrategy {

    private final ExpenseRepository expenseRepository;

    private final NotificationService notificationService;

    private final TripService tripService;

    private final UserProfileService userProfileService;

    public CollectExpense(ExpenseRepository expenseRepository, NotificationService notificationService,
                                 TripService tripService, UserProfileService userProfileService) {
        this.expenseRepository = expenseRepository;
        this.notificationService = notificationService;
        this.tripService = tripService;
        this.userProfileService = userProfileService;
    }

    @Override
    public void calculate(Expense expense) throws CurrencyConverterException {
        if (!expense.getCurrency().equals("RON")) {
            Currency.fromString(expense.getCurrency());
        }
        Trip trip = tripService.getTrip(expense.getTrip().getId());
        UserProfile userProfile = userProfileService.getById(expense.getUser().getId());
        expense.setTrip(trip);
        expense.setUser(userProfile);

        Expense initialExpense = expenseRepository.findByProductAndExpensiveTypeAndTripId(expense.getProduct(), StringUtils.INITIAL_COLLECT_EXPENSE, expense.getTrip().getId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find initial collect expense for " + expense.getProduct()));
        List<Expense> userExpenses = expenseRepository.findByProductAndExpensiveTypeAndUserIdAndTripId(expense.getProduct(), StringUtils.COLLECT_EXPENSE,
                expense.getUser().getId(), expense.getTrip().getId());

        Currency currency;
        if (!initialExpense.getCurrency().equals("RON")) {
            currency = Currency.fromString(initialExpense.getCurrency());
        } else {
            currency = Currency.MDL;
        }
        float total = ExpenseUtils.calculateTotalExpenses(userExpenses, currency);
        if (total >= initialExpense.getSum() / trip.getMembers().size()) {
            throw new IllegalExpenseException("The sum for " + expense.getProduct() + "has been already paid.");
        }

        userExpenses.add(expense);
        float newTotal = ExpenseUtils.calculateTotalExpenses(userExpenses, currency);
        float percent = newTotal / (initialExpense.getSum() / trip.getMembers().size());
        if (percent > 1.05) {
            float remainingSum = initialExpense.getSum() / trip.getMembers().size() - total;
            throw new IllegalExpenseException("User has to pay " + remainingSum + " " + initialExpense.getCurrency() +
                    " for " + initialExpense.getProduct() + ". The sum is too big.");
        }

        expenseRepository.save(expense);

        List<Expense> expenses = expenseRepository.findByProductAndTripIdAndExpensiveType(expense.getProduct(),
                expense.getTrip().getId(), StringUtils.COLLECT_EXPENSE);
        if (!initialExpense.getCurrency().equals("RON")) {
            currency = Currency.fromString(initialExpense.getCurrency());
        } else {
            currency = Currency.MDL;
        }
        float totalSum = ExpenseUtils.calculateTotalExpenses(expenses, currency);
        float finalPercent = totalSum / initialExpense.getSum() * 100;

        String notificationType = finalPercent < initialExpense.getPercent() ?
                NotificationTemplate.COLLECT_SUM : NotificationTemplate.COLLECT_SUM_SUCCESSFULLY;
        notificationService.saveCollectExpensiveNotification(expense, String.valueOf(finalPercent), notificationType, initialExpense.getUser());
    }
}
