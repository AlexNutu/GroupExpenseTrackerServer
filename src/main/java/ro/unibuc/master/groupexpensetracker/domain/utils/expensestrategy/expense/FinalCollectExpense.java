package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.expense;

import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
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
import java.util.Optional;

public class FinalCollectExpense implements ExpenseStrategy {

    private final ExpenseRepository expenseRepository;

    private final NotificationService notificationService;

    private final TripService tripService;

    private final UserProfileService userProfileService;

    public FinalCollectExpense(ExpenseRepository expenseRepository, NotificationService notificationService,
                          TripService tripService, UserProfileService userProfileService) {
        this.expenseRepository = expenseRepository;
        this.notificationService = notificationService;
        this.tripService = tripService;
        this.userProfileService = userProfileService;
    }

    @Override
    public void calculate(Expense expense) throws CurrencyConverterException {
        Trip trip = tripService.getTrip(expense.getTrip().getId());
        UserProfile user = userProfileService.getById(expense.getUser().getId());
        expense.setTrip(trip);
        expense.setUser(user);

        Expense initialExpense = expenseRepository.findByProductAndExpensiveTypeAndTripId(expense.getProduct(), StringUtils.INITIAL_COLLECT_EXPENSE, expense.getTrip().getId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find initial collect expense for " + expense.getProduct()));
        Optional<Expense> finalExpense = expenseRepository.findByProductAndExpensiveTypeAndTripId(expense.getProduct(), StringUtils.FINAL_COLLECT_EXPENSE, expense.getTrip().getId());
        if (finalExpense.isPresent()) {
            throw new EntityNotFoundException("Final collect expense for " + expense.getProduct() + " has been already made.");
        }
        if (!initialExpense.getUser().equals(expense.getUser())) {
            throw new IllegalExpenseException("Unauthorized to perform the expense");
        }

        List<Expense> expenses = expenseRepository.findByProductAndTripIdAndExpensiveType(expense.getProduct(),
                expense.getTrip().getId(), StringUtils.COLLECT_EXPENSE);
        Currency currency;
        if (!initialExpense.getCurrency().equals("RON")) {
            currency = Currency.fromString(initialExpense.getCurrency());
        } else {
            currency = Currency.MDL;
        }
        float totalSum = ExpenseUtils.calculateTotalExpenses(expenses, currency);
        float finalPercent = totalSum / initialExpense.getSum() * 100;

        if (finalPercent > initialExpense.getPercent()) {
            expenseRepository.save(expense);
            for (UserProfile userProfile : trip.getMembers()) {
                if (!userProfile.equals(expense.getUser())) {
                    notificationService.saveExpenseNotification(expense, userProfile);
                }
            }
        }
    }
}
