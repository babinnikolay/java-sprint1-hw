import repository.MonthAccountingEntry;
import repository.ReportRepository;
import repository.YearAccountingEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportChecker {
    private final ReportRepository reportRepository;

    public ReportChecker(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void checkReports() {

        Map<Date, List<MonthAccountingEntry>> monthlyReports = reportRepository.getMonthlyReports();
        Map<Integer, List<YearAccountingEntry>> yearlyReports = reportRepository.getYearlyReports();

        if (monthlyReports.isEmpty()) {
            System.out.println("Данные месячных отчетов отсутствуют");
            return;
        }

        if (yearlyReports.isEmpty()) {
            System.out.println("Данные годовых отчетов отсутствуют");
            return;
        }

        for (Map.Entry<Integer, List<YearAccountingEntry>> yearEntry : yearlyReports.entrySet()) {

            checkYearlyDataAndPrintResult(yearEntry.getKey(), yearEntry.getValue(), monthlyReports);

        }

    }

    private void checkYearlyDataAndPrintResult(int year, List<YearAccountingEntry> yearAccountingEntries,
                                               Map<Date, List<MonthAccountingEntry>> monthlyReports) {

        Map<Integer, MonthlyTotalsEntry> monthlyTotals = new HashMap<>();
        fillMonthlyTotalsForYear(year, monthlyReports, monthlyTotals);

        List<String> messages = new LinkedList<>();
        checkTotalsAndFillMessages(year, yearAccountingEntries, monthlyTotals, messages);

        for (String message : messages) {
            System.out.println(message);
        }

    }

    private void checkTotalsAndFillMessages(int year, List<YearAccountingEntry> yearAccountingEntries, Map<Integer, MonthlyTotalsEntry> monthlyTotals, List<String> messages) {
        for (YearAccountingEntry accountingEntry : yearAccountingEntries) {

            int month = accountingEntry.getMonth();
            if (monthlyTotals.containsKey(month)) {

                double monthlySum = 0d;
                if (accountingEntry.isExpensive()) {
                    monthlySum = monthlyTotals.get(month).getExpense();
                } else {
                    monthlySum = monthlyTotals.get(month).getIncome();
                }

                if (accountingEntry.getSum() != monthlySum) {

                    String message = String.format("Сумма %,.2f в месячном отчете за %s " +
                                            "не равна сумме %,.2f в годовом отчете за %d",
                                    monthlySum,
                                    monthlyTotals.get(month).getMonthView(),
                                    accountingEntry.getSum(),
                                    year);

                    messages.add(message);

                }

            }

        }

        if (messages.isEmpty()) {
            messages.add("Данные в годовом и месячных отчетах соответствуют");
        }

    }

    private void fillMonthlyTotalsForYear(int year,
                                          Map<Date, List<MonthAccountingEntry>> monthlyReports,
                                          Map<Integer, MonthlyTotalsEntry> monthlyTotals) {

        Calendar calendar = Calendar.getInstance();
        int monthlyYear;
        for (Map.Entry<Date, List<MonthAccountingEntry>> monthlyEntry : monthlyReports.entrySet()) {

            calendar.setTime(monthlyEntry.getKey());
            monthlyYear = calendar.get(Calendar.YEAR);

            if (monthlyYear != year) {
                continue;
            }

            MonthlyTotalsEntry totalsEntry;
            int month = calendar.get(Calendar.MONTH) + 1;

            if (monthlyTotals.containsKey(month)) {
                totalsEntry = monthlyTotals.get(month);
            } else {
                totalsEntry = new MonthlyTotalsEntry(month, 0d, 0d);
            }

            for (MonthAccountingEntry monthAccountingEntry : monthlyEntry.getValue()) {
                if (monthAccountingEntry.isExpense()) {
                    totalsEntry.increaseExpense(monthAccountingEntry.getCost());
                } else {
                    totalsEntry.increaseIncome(monthAccountingEntry.getCost());
                }
            }
            monthlyTotals.put(month, totalsEntry);
        }
    }

    private class MonthlyTotalsEntry {
        private String monthView;
        private Integer month;
        private double expense;
        private double income;

        public double getExpense() {
            return expense;
        }

        public double getIncome() {
            return income;
        }

        public String getMonthView() {
            return monthView;
        }

        public MonthlyTotalsEntry(Integer month, double expense, double income) {
            this.month = month;
            this.expense = expense;
            this.income = income;

            try {
                Date date = new SimpleDateFormat("M").parse(String.valueOf(month));
                this.monthView = new SimpleDateFormat("MMMM").format(date);
            } catch (ParseException e) {

            }
        }

        public double increaseExpense(double expense) {
            this.expense += expense;
            return this.expense;
        }

        public double increaseIncome(double income) {
            this.income += income;
            return this.income;
        }

    }
}
