package ro.unibuc.master.groupexpensetracker.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
    Optional<Expense> findByProductAndExpensiveTypeAndTripId(String product, String expensiveType, long tripId);

    List<Expense> findByProductAndExpensiveTypeAndUserIdAndTripId(String product, String expensiveType, long userId, long tripId);

    List<Expense> findByProductAndTripIdAndExpensiveType(String product, long tripId, String expensiveType);

    List<Expense> findByTripIdAndExpensiveType(long tripId, String expensiveType);
}
