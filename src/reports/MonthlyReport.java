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

        for (Map.Entry<Date, List<MonthAccountingEntry>> entry : monthlyReports.entrySet()) {

            System.out.println(new SimpleDateFormat("yyyy LLLL").format(entry.getKey()));

            double maxCost = 0;
            double minCost = 0;
            MonthAccountingEntry mostProfitableItem = null;
            MonthAccountingEntry mostUnprofitableItem = null;

            for (MonthAccountingEntry accountingEntry : entry.getValue()) {

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

            if (mostProfitableItem != null) {
                System.out.printf("Самый прибыльный товар: %s на сумму %,.2f\n",
                        mostProfitableItem.getName(), maxCost);
            }

            if (mostUnprofitableItem != null) {
                System.out.printf("Самый убыточный товар: %s на сумму %,.2f\n",
                        mostUnprofitableItem.getName(), minCost);
            }

            System.out.println(reportDelimiter);

        }
    }
}
