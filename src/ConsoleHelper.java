import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleHelper {

    Scanner scanner;
    public ConsoleHelper() {
         scanner = new Scanner(System.in);
    }

    public void showMainMenu() {

        System.out.println();
        System.out.println("Выберите действие:");
        System.out.println("1. Считать все месячные отчёты");
        System.out.println("2. Считать годовой отчёт");
        System.out.println("3. Сверить отчёты");
        System.out.println("4. Вывести информацию о всех месячных отчётах");
        System.out.println("5. Вывести информацию о годовом отчёте");
        System.out.println("0. Выход");

    }


    public int readUserCommand() {

        int userCommand;
        try {
            userCommand = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }

        return userCommand;

    }
}
