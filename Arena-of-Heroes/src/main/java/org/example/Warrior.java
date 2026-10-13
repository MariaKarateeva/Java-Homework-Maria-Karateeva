package org.example;

public class Warrior extends Hero implements Castable, Restable{

    private final int armor;

    public Warrior(String name, int maxHealth, int baseAttack, int armor){
        super(name, maxHealth, baseAttack);
        if (armor < MIN_STAT_VALUE){
            armor = MIN_STAT_VALUE;

        }
        this.armor = armor;
    }

    public int getArmor(){
        return armor;
    }

    @Override
    public void takeDamage(int damage){
        int finalDamage = damage - armor;
        if (finalDamage < 1){
            finalDamage = 1;
        }
        super.takeDamage(finalDamage);
    }

    @Override
    public void attack(Hero target){
        System.out.println(getName() + " бьёт мечём героя " + target.getName() + " атака: " + getBaseAttack() + ")");
        target.takeDamage(getBaseAttack());
    }

    @Override
    public ActionType makeTurn(Hero target){
        if (canCast()){
            castSpecialSkill(target);
            return ActionType.SPECIAL_SKILL;
        }
        if (needsRest()){
            rest();
            return ActionType.REST;
        }
        attack(target);
        return ActionType.BASE_ATTACK;
    }

    @Override
    public boolean canCast(){
        return getHealth()*100 < getMaxHealth()*50;
    }

    @Override
    public void castSpecialSkill(Hero target){
        if (!canCast()){
            System.out.println("Воин попытался ударить щитом, но потерял равновесие!");
            return;
        }
        int damage = getBaseAttack() + armor*2;
        System.out.println(getName() + " бьёт щитом героя " + target.getName() + " (урон: " + damage + ")");
        target.takeDamage(damage);
    }

    @Override
    public boolean needsRest(){
        return getHealth()*100 < getMaxHealth()*15;
    }

    @Override
    public void rest(){
        int healAmount = armor*2;
        heal(healAmount);
        System.out.println(getName() + "переводит дух и лечится на " + healAmount);
    }

    @Override
    public String getStatus(){
        return super.getStatus() + ", броня" + armor;
    }
}