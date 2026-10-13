package org.example;

import java.util.Scanner;

public class Main {

    private static final int MAX_VALUE = 10000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Hero first = createHero(scanner, 1);
        Hero second = createHero(scanner, 2);

        Arena arena = new Arena(first, second);
        arena.startTournament();
    }

    private static Hero createHero(Scanner scanner, int number) {
        System.out.println();
        System.out.println("=== Создание бойца №" + number + " ===");

        int type = readNumber(scanner, "Тип персонажа (1 - Воин, 2 - Маг): ", 1, 2);

        System.out.print("Имя (одним словом): ");
        String name = scanner.next();

        int maxHealth = readNumber(scanner, "Максимальное здоровье: ", Hero.MIN_STAT_VALUE, MAX_VALUE);
        int baseAttack = readNumber(scanner, "Базовая атака: ", Hero.MIN_STAT_VALUE, MAX_VALUE);

        if (type == 1) {
            int armor = readNumber(scanner, "Броня: ", Hero.MIN_STAT_VALUE, MAX_VALUE);
            return new Warrior(name, maxHealth, baseAttack, armor);
        } else {
            int maxMana = readNumber(scanner, "Максимальная мана: ", Hero.MIN_STAT_VALUE, MAX_VALUE);
            return new Mage(name, maxHealth, baseAttack, maxMana);
        }
    }

    private static int readNumber(Scanner scanner, String question, int min, int max) {
        int number = 0;
        boolean correct = false;

        while (!correct) {
            System.out.print(question);
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
                if (number >= min && number <= max) {
                    correct = true;
                } else {
                    System.out.println("Число должно быть от " + min + " до " + max + ".");
                }
            } else {
                System.out.println("Это не число, попробуйте ещё раз.");
                scanner.next();
            }
        }

        return number;
    }
}