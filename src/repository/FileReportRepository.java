package repository;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileReportRepository implements ReportRepository {

    private final String reportsFolderPath = "resources";
    private final String fieldsSeparator = ",";
    private File resourceFolder;

    private Map<Date, List<MonthAccountingEntry>> monthlyReports;
    private Map<Integer, List<YearAccountingEntry>> yearlyReports;

    private FileReader fileReader;

    public FileReportRepository() {
        monthlyReports = new TreeMap<>();
        yearlyReports = new HashMap<>();

        resourceFolder = new File(reportsFolderPath);
        fileReader = new FileReader();
    }

    @Override
    public void readMonthlyReports() {

        monthlyReports.clear();
        fillMonthlyReportsFromFiles();

    }

    @Override
    public void readYearReports() {

        yearlyReports.clear();
        fillYearlyReportsFromFiles();

    }

    @Override
    public boolean isEmptyMonthlyData() {
        return monthlyReports.isEmpty();
    }

    @Override
    public Map<Date, List<MonthAccountingEntry>> getMonthlyReports() {
        return monthlyReports;
    }

    @Override
    public boolean isEmptyYearlyData() {
        return yearlyReports.isEmpty();
    }

    @Override
    public Map<Integer, List<YearAccountingEntry>> getYearlyReports() {
        return yearlyReports;
    }

    private void fillYearlyReportsFromFiles() {

        File[] files = resourceFolder.listFiles();
        if (files != null) {
            for (final File fileEntry : files) {
                if (fileEntry.isFile() && fileEntry.getName().toLowerCase(Locale.ROOT).startsWith("y.")) {

                    List<String> fileContents = fileReader.readFileContentsOrNull(fileEntry.getPath());
                    if (fileContents == null) {
                        continue;
                    }

                    int yearOfFile = Integer.parseInt(fileEntry.getName().split("\\.")[1]);

                    List<YearAccountingEntry> yearEntries = new ArrayList<>();
                    for (int i = 1; i < fileContents.size(); i++) {

                        String[] entryFields = fileContents.get(i).split(fieldsSeparator);

                        YearAccountingEntry yearEntry = new YearAccountingEntry(
                                Integer.valueOf(entryFields[0]),
                                Double.valueOf(entryFields[1]),
                                Boolean.valueOf(entryFields[2]));

                        yearEntries.add(yearEntry);
                    }

                    yearlyReports.put(yearOfFile, yearEntries);

                }
            }
            System.out.println("Данные отчетов прочитаны");
        } else {
            System.out.printf("Файлы годовых отчетов в папке %s не найдены\n", reportsFolderPath);
        }

    }

    private void fillMonthlyReportsFromFiles() {

        File[] files = resourceFolder.listFiles();
        if (files != null) {
            for (final File fileEntry : files) {

                if (fileEntry.isFile() && fileEntry.getName().toLowerCase(Locale.ROOT).startsWith("m.")) {

                    List<String> fileContents = fileReader.readFileContentsOrNull(fileEntry.getPath());
                    if (fileContents == null) {
                        continue;
                    }

                    String monthOfFile = fileEntry.getName().split("\\.")[1];
                    Date month = null;
                    try {
                        month = new SimpleDateFormat("yyyyM").parse(monthOfFile);
                    } catch (ParseException e) {
                        continue;
                    }

                    List<MonthAccountingEntry> monthEntries = new ArrayList<>();
                    for (int i = 1; i < fileContents.size(); i++) {

                        String[] entryFields = fileContents.get(i).split(fieldsSeparator);

                        MonthAccountingEntry monthEntry = new MonthAccountingEntry(
                                entryFields[0],
                                Boolean.valueOf(entryFields[1]),
                                Double.valueOf(entryFields[2]),
                                Double.valueOf(entryFields[3]));

                        monthEntries.add(monthEntry);
                    }
                    monthlyReports.put(month, monthEntries);

                }
            }

            System.out.println("Данные отчетов прочитаны");

        } else {
            System.out.printf("Файлы месячных отчетов в папке %s не найдены\n", reportsFolderPath);
        }

    }

    private class FileReader {

        List<String> readFileContentsOrNull(String path) {

            List<String> result = new LinkedList<>();

            try (BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(path))) {

                String line;
                while ((line = bufferedReader.readLine()) != null){
                    result.add(line);
                }

            } catch (IOException e) {
                System.out.printf("Ошибка чтения файла %s", path);
                return null;
            }

            return result;

        }

    }

}
