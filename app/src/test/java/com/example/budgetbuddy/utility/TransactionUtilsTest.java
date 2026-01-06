package com.example.budgetbuddy.utility;

import androidx.core.util.Pair;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.entities.Category;
import com.example.budgetbuddy.entities.Transaction;
import com.example.budgetbuddy.entities.TransactionWithCategory;
import com.example.budgetbuddy.enums.RepeatDuration;
import com.example.budgetbuddy.enums.TransactionType;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TransactionUtilsTest extends TestCase {
    private List<TransactionWithCategory> transactions;
    private Category cat1, cat2, cat3, cat4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Setup categories
        cat1 = new Category("Groceries", R.color.red);
        cat2 = new Category("Utilities", R.color.blue);
        cat3 = new Category("Entertainment", R.color.green);
        cat4 = new Category("Income", R.color.yellow);

        // Setup transactions
        transactions = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        // Outgoing transactions
        cal.set(2023, Calendar.JANUARY, 10);
        transactions.add(new TransactionWithCategory(new Transaction(100, TransactionType.OUTGOING, (Calendar) cal.clone(), 0, RepeatDuration.NEVER), cat1));
        cal.set(2023, Calendar.JANUARY, 12);
        transactions.add(new TransactionWithCategory(new Transaction(50, TransactionType.OUTGOING, (Calendar) cal.clone(), 0, RepeatDuration.NEVER), cat2));
        cal.set(2023, Calendar.JANUARY, 5);
        transactions.add(new TransactionWithCategory(new Transaction(25, TransactionType.OUTGOING, (Calendar) cal.clone(), 0, RepeatDuration.NEVER), cat1));
        cal.set(2023, Calendar.JANUARY, 15);
        transactions.add(new TransactionWithCategory(new Transaction(75, TransactionType.OUTGOING, (Calendar) cal.clone(), 0, RepeatDuration.NEVER), cat3));

        // Incoming transaction
        cal.set(2023, Calendar.JANUARY, 1);
        transactions.add(new TransactionWithCategory(new Transaction(1000, TransactionType.INCOMING, (Calendar) cal.clone(), 0, RepeatDuration.NEVER), cat4));
    }

    public void testSortTransactions() {
        List<TransactionWithCategory> sorted = TransactionUtils.sortTransactions(new ArrayList<>(transactions));

        // Should be sorted in descending order of date
        assertEquals("Entertainment", sorted.get(0).category.getName()); // Jan 15
        assertEquals("Utilities", sorted.get(1).category.getName()); // Jan 12
        assertEquals("Groceries", sorted.get(2).category.getName()); // Jan 10
        assertEquals("Groceries", sorted.get(3).category.getName()); // Jan 5
        assertEquals("Income", sorted.get(4).category.getName()); // Jan 1
    }

    public void testSelectRepeatDuration() {
        assertEquals(RepeatDuration.NEVER, TransactionUtils.selectRepeatDuration("Never"));
        assertEquals(RepeatDuration.DAILY, TransactionUtils.selectRepeatDuration("Day"));
        assertEquals(RepeatDuration.WEEKLY, TransactionUtils.selectRepeatDuration("Week"));
        assertEquals(RepeatDuration.MONTHLY, TransactionUtils.selectRepeatDuration("Month"));
        assertEquals(RepeatDuration.YEARLY, TransactionUtils.selectRepeatDuration("Year"));
        assertNull(TransactionUtils.selectRepeatDuration("Invalid"));
    }

    public void testGetTotalSpend() {
        double totalSpend = TransactionUtils.getTotalSpend(transactions);

        // Should only sum outgoings: 100 + 50 + 25 + 75 = 250
        assertEquals(250.0, totalSpend);

        // test empty list
        assertEquals(0.0, TransactionUtils.getTotalSpend(new ArrayList<>()));
    }

    public void testGetBudgetRemaining() {
        double startBudget = 500.0;

        double remaining = TransactionUtils.getBudgetRemaining(startBudget, transactions);


        // 500 + 1000 - 250 = 1250
        assertEquals(1250.0, remaining);

        // test empty list
        assertEquals(startBudget, TransactionUtils.getBudgetRemaining(startBudget, new ArrayList<>()));
    }

    public void testGetCategoryTotals() {
        Map<Category, Double> totals = TransactionUtils.getCategoryTotals(transactions);

        // Should only contain outgoing transactions, aggregated by category
        assertEquals(3, totals.size()); // Groceries, Utilities, Entertainment
        assertEquals(125.0, totals.get(cat1)); // Groceries: 100 + 25
        assertEquals(50.0, totals.get(cat2));  // Utilities: 50
        assertEquals(75.0, totals.get(cat3));  // Entertainment: 75
        assertNull(totals.get(cat4));          // Income category should not be present
    }

    public void testGetSortedCategoryTotals() {
        List<Map.Entry<Category, Double>> sortedTotals = TransactionUtils.getSortedCategoryTotals(transactions);

        // Should be sorted in descending order
        assertEquals(3, sortedTotals.size());
        assertEquals(cat1, sortedTotals.get(0).getKey()); // Groceries: 125.0
        assertEquals(125.0, sortedTotals.get(0).getValue());
        assertEquals(cat3, sortedTotals.get(1).getKey()); // Entertainment: 75.0
        assertEquals(75.0, sortedTotals.get(1).getValue());
        assertEquals(cat2, sortedTotals.get(2).getKey()); // Utilities: 50.0
        assertEquals(50.0, sortedTotals.get(2).getValue());
    }

    public void testGetTopNCategoryTotals() {
        int topN = 2;

        Pair<List<Map.Entry<Category, Double>>, Map.Entry<String, Double>> result =
                TransactionUtils.getTopNCategoryTotals(transactions, topN);

        List<Map.Entry<Category, Double>> namedCategories = result.first;
        Map.Entry<String, Double> otherCategory = result.second;

        // Named categories should be the top 2
        assertEquals(2, namedCategories.size());
        assertEquals(cat1, namedCategories.get(0).getKey()); // Groceries: 125.0
        assertEquals(cat3, namedCategories.get(1).getKey()); // Entertainment: 75.0

        // "Other" should be the sum of the rest
        assertEquals("Other", otherCategory.getKey());
        assertEquals(50.0, otherCategory.getValue());

        // N larger than categories
        result = TransactionUtils.getTopNCategoryTotals(transactions, 5);
        assertEquals(3, result.first.size()); // Should just return all 3 categories
        assertEquals(0.0, result.second.getValue());   // Other total should be 0

        // test empty list
        result = TransactionUtils.getTopNCategoryTotals(Collections.emptyList(), topN);
        assertNotNull(result);
        assertTrue(result.first.isEmpty());
        assertEquals("Other", result.second.getKey());
        assertEquals(0.0, result.second.getValue());
    }
}
