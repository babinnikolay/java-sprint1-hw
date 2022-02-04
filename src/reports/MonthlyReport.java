package reports;

import repository.MonthAccountingEntry;
import repository.ReportRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MonthlyReport implements Report{
    private final ReportRepository repository;

    private final String reportDelimiter = "********************************";

    public MonthlyReport(ReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public void print() {

        if (repository.isEmptyMonthlyData()) {
            System.out.println("Данные месячных отчетов отсутствуют");
            return;
        }

        Map<Date, List<MonthAccountingEntry>> monthlyReports = repository.getMonthlyReports();

        printReportForEachMonth(monthlyReports);

    }

    private void printReportForEachMonth(Map<Date, List<MonthAccountingEntry>> monthlyReports) {
        for (Map.Entry<Date, List<MonthAccountingEntry>> monthlyEntry : monthlyReports.entrySet()) {

            System.out.println(new SimpleDateFormat("yyyy LLLL").format(monthlyEntry.getKey()));
            
            MonthAccountingEntry mostProfitableItem = null;
            MonthAccountingEntry mostUnprofitableItem = null;
            double maxCost = 0;
            double minCost = 0;
            
            for (MonthAccountingEntry accountingEntry : monthlyEntry.getValue()) {

                if (accountingEntry.isExpense()) {
                    if (accountingEntry.getCost() > minCost) {
                        minCost = accountingEntry.getCost();
                        mostUnprofitableItem = accountingEntry;
                    }
                } else {
                    if (accountingEntry.getCost() > maxCost) {
                        maxCost = accountingEntry.getCost();
                        mostProfitableItem = accountingEntry;
                    }
                }

            }

            printMonthlyResult(mostProfitableItem, mostUnprofitableItem);

        }
    }

    private void printMonthlyResult(MonthAccountingEntry mostProfitableItem, 
                                    MonthAccountingEntry mostUnprofitableItem) {
        
        if (mostProfitableItem != null) {
            System.out.printf("Самый прибыльный товар: %s на сумму %,.2f%s",
                    mostProfitableItem.getName(),
                    mostProfitableItem.getCost(),
                    System.lineSeparator());
        }

        if (mostUnprofitableItem != null) {
            System.out.printf("Самый убыточный товар: %s на сумму %,.2f%s",
                    mostUnprofitableItem.getName(),
                    mostUnprofitableItem.getCost(),
                    System.lineSeparator());
        }

        System.out.println(reportDelimiter);
        
    }
}
