package bank.ATM;

import java.util.Scanner;

public class Main {
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CashMachine atm = new CashMachine();
        Account refAccount = new Account(12345, 999, 10000.00, BankType.AUM);

        System.out.println("Добро пожаловать в банкомат!");
        System.out.print("Введите номер карты (5 цифр): ");

        if (!scanner.hasNextInt()) {
            System.out.println("Ошибка: Введены не цифры. Доступ запрещён.");
            return;
        }
        int inputCard = scanner.nextInt();
        System.out.print("Введите пин-код (3 цифры): ");
        if (!scanner.hasNextInt()) {
            System.out.println("ОшибкаЖ Введены не цифры. Доступ запрещёню");
            return;
        }
        int inputPin = scanner.nextInt();

        if (inputCard != refAccount.getCardNumber() || inputPin != refAccount.getPinCide()) {
            System.out.println("Ошибка доступа: Неверный номер карты или пин-код");
            return;
        }

        System.out.println("Авторизация успешна!");
        System.out.println("Текущий баланс: " + refAccount.getBalance() + " руб.");

        System.out.print("Введите сумму для внесения: ");
        if (scanner.hasNextDouble()) {
            double depositAmount = scanner.nextDouble();

            double newBalance = atm.deposit(refAccount.getBalance(), depositAmount);

            refAccount.balance = newBalance;

            System.out.println("Баланс после внесения: " + newBalance + " руб.");
        } else {
            System.out.println("Некорректная сумма. Пропуск операции.");
        }

        System.out.print("Введите сумму для снятия: ");
        if (scanner.hasNextDouble()) {
            double withdrawAmount = scanner.nextDouble();
            double newBalance = atm.withdraw(refAccount.getBalance(), withdrawAmount, refAccount.getBankType());
            refAccount.balance = newBalance;

            System.out.println("Итоговый баланс: " + newBalance + " руб.");
            System.out.println("Детали: " + refAccount.toString());
        } else {
            System.out.println("Некорректная сумма. Пропуск операции.");

            scanner.close();
        }
    }
}
