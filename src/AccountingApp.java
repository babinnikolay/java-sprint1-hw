import reports.MonthlyReport;
import reports.Report;
import reports.YearlyReport;
import repository.FileReportRepository;
import repository.ReportRepository;

public class AccountingApp {
    private final ConsoleHelper consoleHelper;
    private final ReportRepository repository;
    private final Report monthlyReport;
    private final Report yearlyReport;
    private final ReportChecker reportChecker;

    private final int READ_MONTHLY = 1;
    private final int READ_YEARLY = 2;
    private final int CHECK_REPORTS = 3;
    private final int PRINT_MONTHLY = 4;
    private final int PRINT_YEARLY = 5;
    private final int EXIT = 0;

    public AccountingApp() {
        consoleHelper = new ConsoleHelper();
        repository = new FileReportRepository();
        monthlyReport = new MonthlyReport(repository);
        yearlyReport = new YearlyReport(repository);
        reportChecker = new ReportChecker(repository);
        sayHello();
    }

    private void sayHello() {

        double appVersion = 1.0;
        System.out.printf("Программа бухгалтерского учета %.1f\n", appVersion);
        System.out.println("Для выхода из программы выберите пункт меню.");

    }

    public void run() {

        while (true) {

            consoleHelper.showMainMenu();
            int userCommand = consoleHelper.readUserCommand();

            switch (userCommand) {
                case READ_MONTHLY:
                    repository.readMonthlyReports();
                    continue;
                case READ_YEARLY:
                    repository.readYearReports();
                    continue;
                case CHECK_REPORTS:
                    reportChecker.checkReports();
                    continue;
                case PRINT_MONTHLY:
                    monthlyReport.print();
                    continue;
                case PRINT_YEARLY:
                    yearlyReport.print();
                    continue;
                case EXIT:
                    return;
                default:
                    continue;
            }

        }
    }
}
