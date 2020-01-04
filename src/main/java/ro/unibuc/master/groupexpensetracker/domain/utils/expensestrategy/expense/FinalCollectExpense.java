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
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.ExpenseStrategy;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;
import ro.unibuc.master.groupexpensetracker.exception.IllegalExpenseException;

import java.util.List;

public class FinalCollectExpense implements ExpenseStrategy {

    private final ExpenseRepository expenseRepository;

    private final NotificationService notificationService;

    private final TripService tripService;

    public FinalCollectExpense(ExpenseRepository expenseRepository, NotificationService notificationService, TripService tripService) {
        this.expenseRepository = expenseRepository;
        this.notificationService = notificationService;
        this.tripService = tripService;
    }

    @Override
    public void calculate(Expense expense) throws CurrencyConverterException {
        Trip trip = tripService.getTrip(expense.getTrip().getId());
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
        float total = ExpenseUtils.calculateTotalExpenses(expenses, currency);
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
}
