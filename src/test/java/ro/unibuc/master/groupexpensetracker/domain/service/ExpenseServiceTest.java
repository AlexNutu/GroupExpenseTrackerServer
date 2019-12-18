package ro.unibuc.master.groupexpensetracker.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.unibuc.master.groupexpensetracker.common.TestUtils;
import ro.unibuc.master.groupexpensetracker.data.expense.Expense;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ExpenseServiceTest {

    @Autowired
    ExpenseService expenseService;

    @Test
    public void testProcessExpense() {
        Expense expense = TestUtils.getMockedExpense();
        expenseService.processExpense(expense);
    }
}
