package ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy;

import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;
import ro.unibuc.master.groupexpensetracker.domain.service.NotificationService;
import ro.unibuc.master.groupexpensetracker.domain.service.TripService;

public class SimpleExpense implements ExpenseStrategy {

    private final ExpenseRepository expenseRepository;

    private final NotificationService notificationService;

    private final TripService tripService;

    public SimpleExpense(ExpenseRepository expenseRepository, NotificationService notificationService, TripService tripService) {
        this.expenseRepository = expenseRepository;
        this.notificationService = notificationService;
        this.tripService = tripService;
    }

    @Override
    public void calculate(Expense expense) throws CurrencyConverterException {
        if (!expense.getCurrency().equals("RON")) {
            Currency.fromString(expense.getCurrency());
        }
        expenseRepository.save(expense);
        Trip trip = tripService.getTrip(expense.getTrip().getId());
        notificationService.saveSimpleExpenseNotification(expense, trip.getMembers());
    }
}
