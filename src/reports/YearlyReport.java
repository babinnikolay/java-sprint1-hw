package reports;

import repository.ReportRepository;
import repository.YearAccountingEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearlyReport implements Report {

    private ReportRepository repository;

    private final String reportDelimiter = "********************************";

    public YearlyReport(ReportRepository repository) {
        this.repository = repository;
    }



    @Override
    public void print() {

        if (repository.isEmptyYearlyData()) {
            System.out.println("Данные годовых отчетов отсутствуют");
            return;
        }

        Map<Integer, List<YearAccountingEntry>> yearlyReports = repository.getYearlyReports();

        printReportForEachYear(yearlyReports);

    }

    private void printReportForEachYear(Map<Integer, List<YearAccountingEntry>> yearlyReports) {
        for (Map.Entry<Integer, List<YearAccountingEntry>> yearEntry : yearlyReports.entrySet()) {

            Map<Integer, Double> monthlyProfit = new HashMap<>();
            Double expenseSum = 0d;
            int expenseCount = 0;

            Double incomeSum = 0d;
            int incomeCount = 0;

            for (YearAccountingEntry accountingEntry : yearEntry.getValue()) {

                if (accountingEntry.isExpensive()) {
                    expenseSum += accountingEntry.getSum();
                    expenseCount++;
                } else {
                    incomeSum += accountingEntry.getSum();
                    incomeCount++;
                }

                int month = accountingEntry.getMonth();
                addSumForMonth(monthlyProfit, accountingEntry, month);

            }

            printYearlyResult(yearEntry, monthlyProfit, expenseSum, expenseCount, incomeSum, incomeCount);

        }
    }

    private void addSumForMonth(Map<Integer, Double> monthlyProfit, YearAccountingEntry accountingEntry, int month) {
        if (monthlyProfit.containsKey(month)) {

            double newMonthSum = monthlyProfit.get(month)
                    + (accountingEntry.isExpensive() ? -accountingEntry.getSum() : accountingEntry.getSum());
            monthlyProfit.put(month, newMonthSum);

        } else {

            monthlyProfit.put(month,
                    accountingEntry.isExpensive() ? - accountingEntry.getSum() : accountingEntry.getSum());

        }
    }

    private void printYearlyResult(Map.Entry<Integer, List<YearAccountingEntry>> yearEntry,
                                   Map<Integer, Double> monthlyProfit,
                                   Double expenseSum,
                                   int expenseCount,
                                   Double incomeSum, int incomeCount) {
        System.out.println(yearEntry.getKey());

        for (Map.Entry<Integer, Double> monthlyEntry : monthlyProfit.entrySet()) {

            Date date = null;
            try {
                date = new SimpleDateFormat("M").parse(monthlyEntry.getKey().toString());
            } catch (ParseException e) {
                continue;
            }

            String month = new SimpleDateFormat("MMMM").format(date);
            System.out.printf("Прибыль за %s составила:%,.2f%s",
                    month,
                    monthlyEntry.getValue(),
                    System.lineSeparator());

        }

        System.out.printf("Средний расход за все месяцы: %,.2f%s", expenseSum / expenseCount, System.lineSeparator());
        System.out.printf("Средний доход за все месяцы: %,.2f%s", incomeSum / incomeCount, System.lineSeparator());
        System.out.println(reportDelimiter);
    }
}
