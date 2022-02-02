package repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportRepository {
    void readMonthlyReports();
    void readYearReports();
    boolean isEmptyMonthlyData();
    Map<Date, List<MonthAccountingEntry>> getMonthlyReports();
    boolean isEmptyYearlyData();
    Map<Integer, List<YearAccountingEntry>> getYearlyReports();
}
