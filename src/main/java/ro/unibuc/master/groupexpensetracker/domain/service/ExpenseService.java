package ro.unibuc.master.groupexpensetracker.domain.service;

import com.tunyk.currencyconverter.api.Currency;
import com.tunyk.currencyconverter.api.CurrencyConverterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.unibuc.master.groupexpensetracker.common.utils.*;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;
import ro.unibuc.master.groupexpensetracker.data.trip.Trip;
import ro.unibuc.master.groupexpensetracker.data.userprofile.UserProfile;
import ro.unibuc.master.groupexpensetracker.domain.repository.ExpenseRepository;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.context.ContextExpense;
import ro.unibuc.master.groupexpensetracker.domain.utils.expensestrategy.expense.*;
import ro.unibuc.master.groupexpensetracker.exception.EntityNotFoundException;
import ro.unibuc.master.groupexpensetracker.exception.IllegalExpenseException;
import ro.unibuc.master.groupexpensetracker.presentation.dto.ExpenseDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final NotificationService notificationService;

    private final TripService tripService;

    private final UserProfileService userProfileService;

    public ExpenseService(ExpenseRepository expenseRepository, NotificationService notificationService,
                          TripService tripService, UserProfileService userProfileService) {
        this.expenseRepository = expenseRepository;
        this.notificationService = notificationService;
        this.tripService = tripService;
        this.userProfileService = userProfileService;
    }

    public ResponseEntity processExpense(Expense expense) throws CurrencyConverterException {
        ContextExpense contextExpense = setContextExpenseByExpenseType(expense);
        contextExpense.executeCalculate(expense);
        return ResponseEntity.ok().build();
    }

    private ContextExpense setContextExpenseByExpenseType(Expense expense) {
        ContextExpense contextExpense;
        switch (expense.getExpensiveType()) {
            case StringUtils.SIMPLE_EXPENSE:
                contextExpense = new ContextExpense(new SimpleExpense(expenseRepository, notificationService, tripService, userProfileService));
                break;
            case StringUtils.GROUP_EXPENSE:
                contextExpense = new ContextExpense(new GroupExpense(expenseRepository, notificationService, tripService, userProfileService));
                break;
            case StringUtils.INITIAL_GROUP_EXPENSE:
                contextExpense = new ContextExpense(new InitialGroupExpense(expenseRepository, notificationService, tripService, userProfileService));
                break;
            case StringUtils.COLLECT_EXPENSE:
                contextExpense = new ContextExpense(new CollectExpense(expenseRepository, notificationService, tripService, userProfileService));
                break;
            case StringUtils.INITIAL_COLLECT_EXPENSE:
                contextExpense = new ContextExpense(new InitialCollectExpense(expenseRepository, notificationService, tripService, userProfileService));
                break;
            case StringUtils.FINAL_COLLECT_EXPENSE:
                contextExpense = new ContextExpense(new FinalCollectExpense(expenseRepository, notificationService, tripService, userProfileService));
                break;
            default:
                throw new IllegalExpenseException("Could not found this type of expensive: " + expense.getExpensiveType());
        }
        return contextExpense;
    }

    public List<ExpenseDTO> findAll(Sort.Direction sortingDirection, String orderBy, final String search, final Integer offset, final Integer size) {
        final List<SearchCriteria> searchCriteriaList = EntityUtils.generateSearchCriteria(search);
        final Specification<Expense> spec = new EntitySpecification<>(searchCriteriaList);

        if (sortingDirection == null && orderBy == null) {
            orderBy = "createDate";
            sortingDirection = Sort.Direction.DESC;
        }

        if (size != null) {
            return expenseRepository.findAll(spec,
                    EntityUtils.getPageRequest(sortingDirection, orderBy, offset, size))
                    .map(Expense::toDto).getContent();
        } else {
            return new PageImpl<>(expenseRepository.findAll(spec)).map(Expense::toDto).getContent();
        }
    }

    public List<ExpenseDTO> getUnperformedExpenses(long tripId, String product) throws CurrencyConverterException {
        List<ExpenseDTO> unperformedExpenses = new ArrayList<>();
        Trip trip = tripService.getTrip(tripId);
        if (trip == null) {
            throw new EntityNotFoundException("Could not find trip by id");
        }
        unperformedExpenses.addAll(getUnperformedGroupExpenses(trip, product));
        unperformedExpenses.addAll(getUnperformedCollectExpenses(trip, product));

        return unperformedExpenses;
    }

    private List<ExpenseDTO> getUnperformedGroupExpenses(Trip trip, String product) throws CurrencyConverterException {
        List<ExpenseDTO> unperformedGroupExpenses = new ArrayList<>();
        List<Expense> groupExpenses;
        if (product != null) {
            groupExpenses = expenseRepository.findByProductAndTripIdAndExpensiveType(product, trip.getId(), StringUtils.INITIAL_GROUP_EXPENSE);
        } else {
            groupExpenses = expenseRepository.findByTripIdAndExpensiveType(trip.getId(), StringUtils.INITIAL_GROUP_EXPENSE);
        }
        for (Expense groupExpense : groupExpenses) {
            for (UserProfile userProfile : trip.getMembers()) {
                if (!groupExpense.getUser().equals(userProfile)) {
                    List<Expense> userExpenses = expenseRepository.findByProductAndExpensiveTypeAndUserIdAndTripId(groupExpense.getProduct(),
                            StringUtils.GROUP_EXPENSE, userProfile.getId(), groupExpense.getTrip().getId());

                    Currency currency;
                    if (!groupExpense.getCurrency().equals("RON")) {
                        currency = Currency.fromString(groupExpense.getCurrency());
                    } else {
                        currency = Currency.MDL;
                    }
                    float difference = groupExpense.getSum() / trip.getMembers().size() - ExpenseUtils.calculateTotalExpenses(userExpenses, currency);
                    if (difference > 0) {
                        ExpenseDTO expenseDTO = new ExpenseDTO();
                        expenseDTO.setCurrency(groupExpense.getCurrency());
                        expenseDTO.setSum(difference);
                        expenseDTO.setProduct(groupExpense.getProduct());
                        expenseDTO.setExpensiveType(StringUtils.GROUP_EXPENSE);
                        expenseDTO.setUser(UserProfile.toDto(userProfile));
                        unperformedGroupExpenses.add(expenseDTO);
                    }
                }
            }
        }
        return unperformedGroupExpenses;
    }

    private List<ExpenseDTO> getUnperformedCollectExpenses(Trip trip, String product) throws CurrencyConverterException {
        List<ExpenseDTO> unperformedCollectExpenses = new ArrayList<>();
        List<Expense> collectExpenses;
        if (product != null) {
            collectExpenses = expenseRepository.findByProductAndTripIdAndExpensiveType(product, trip.getId(), StringUtils.INITIAL_COLLECT_EXPENSE);
        } else {
            collectExpenses = expenseRepository.findByTripIdAndExpensiveType(trip.getId(), StringUtils.INITIAL_COLLECT_EXPENSE);
        }
        for (Expense collectExpense : collectExpenses) {
            for (UserProfile userProfile : trip.getMembers()) {
                List<Expense> userExpenses = expenseRepository.findByProductAndExpensiveTypeAndUserIdAndTripId(collectExpense.getProduct(),
                        StringUtils.COLLECT_EXPENSE, userProfile.getId(), collectExpense.getTrip().getId());

                Currency currency;
                if (!collectExpense.getCurrency().equals("RON")) {
                    currency = Currency.fromString(collectExpense.getCurrency());
                } else {
                    currency = Currency.MDL;
                }
                float difference = collectExpense.getSum() / trip.getMembers().size() - ExpenseUtils.calculateTotalExpenses(userExpenses, currency);
                if (difference > 0) {
                    ExpenseDTO expenseDTO = new ExpenseDTO();
                    expenseDTO.setCurrency(collectExpense.getCurrency());
                    expenseDTO.setSum(difference);
                    expenseDTO.setProduct(collectExpense.getProduct());
                    expenseDTO.setExpensiveType(StringUtils.GROUP_EXPENSE);
                    expenseDTO.setUser(UserProfile.toDto(userProfile));
                    unperformedCollectExpenses.add(expenseDTO);
                }
            }
        }
        return unperformedCollectExpenses;
    }
}
