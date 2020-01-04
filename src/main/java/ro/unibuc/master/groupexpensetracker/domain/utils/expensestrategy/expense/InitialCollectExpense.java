package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.expense;

import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;
import ro.unibuc.master.groupexpensetracker.domain.service.NotificationService;
import ro.unibuc.master.groupexpensetracker.domain.service.TripService;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.ExpenseStrategy;

public class InitialCollectExpense implements ExpenseStrategy {

    private final ExpenseRepository expenseRepository;

    private final NotificationService notificationService;

    private final TripService tripService;

    public InitialCollectExpense(ExpenseRepository expenseRepository, NotificationService notificationService, TripService tripService) {
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
        expenseRepository.save(expense);
        notificationService.saveInitialCollectExpenseNotification(expense, trip.getMembers());
    }
}
