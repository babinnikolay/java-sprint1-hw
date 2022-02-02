package repository;

public class MonthAccountingEntry {
    private final String itemName;
    private final boolean isExpense;
    private final double quantity;
    private final double sumOfOne;

    public MonthAccountingEntry(String itemName, boolean isExpense, double quantity, double sum) {
        this.itemName = itemName;
        this.isExpense = isExpense;
        this.quantity = quantity;
        this.sumOfOne = sum;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public double getCost() {
        return quantity * sumOfOne;
    }

    public String getName() {
        return itemName;
    }
}
