package org.example;

public class Arena {
    private final Hero fighter1;
    private final Hero fighter2;
    private int roundCounter;

    public Arena(Hero fighter1, Hero fighter2) {
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
        this.roundCounter = 0;
    }

    public void startTournament() {
        if (fighter1 == null || fighter2 == null) {
            System.out.println("Для боя нужны два героя!");
            return;
        }

        System.out.println("Бой начинается: " + fighter1.getName() + " против " + fighter2.getName() + "!");

        while (fighter1.isAlive() && fighter2.isAlive()) {
            roundCounter++;
            System.out.println();
            System.out.println("===== Раунд " + roundCounter + " =====");

            Hero attacker;
            Hero defender;
            if (Math.random() < 0.5) {
                attacker = fighter1;
                defender = fighter2;
            } else {
                attacker = fighter2;
                defender = fighter1;
            }

            makeTurnAndReport(attacker, defender);

            if (!defender.isAlive()) {
                break;
            }

            makeTurnAndReport(defender, attacker);

            System.out.println("--- Состояние бойцов ---");
            System.out.println(fighter1.getStatus());
            System.out.println(fighter2.getStatus());
        }

        System.out.println();
        if (fighter1.isAlive()) {
            System.out.println("Победитель: " + fighter1.getName() + "!");
        } else {
            System.out.println("Победитель: " + fighter2.getName() + "!");
        }
    }

    private void makeTurnAndReport(Hero actor, Hero target) {
        ActionType action = actor.makeTurn(target);
        System.out.println("Игрок " + actor.getName() + " совершил действие: " + action.getTitle());

    }
}