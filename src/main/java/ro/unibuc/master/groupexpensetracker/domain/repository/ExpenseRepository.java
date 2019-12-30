package ro.unibuc.master.groupexpensetracker.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
