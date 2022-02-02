package repository;

public class YearAccountingEntry {
    private final int month;
    private final double sum;
    private final boolean isExpensive;

    public YearAccountingEntry(int month, double sum, boolean isExpensive) {
        this.month = month;
        this.sum = sum;
        this.isExpensive = isExpensive;
    }

    public int getMonth() {
        return month;
    }

    public double getSum() {
        return sum;
    }

    public boolean isExpensive() {
        return isExpensive;
    }
}
