package org.example;

public class Mage extends Hero implements Castable, Restable{

    private final int maxMana;
    private int mana;

    public Mage(String name, int maxHealth, int baseAttack, int maxMana){
        super(name, maxHealth, baseAttack);
        if (maxMana < MIN_STAT_VALUE){
            maxMana = MIN_STAT_VALUE;
        }
        this.maxMana = maxMana;
        this.mana = maxMana;
    }

    public int getMaxMana(){
        return maxMana;
    }

    public int getMana(){
        return mana;
    }

    @Override
    public void attack(Hero target){
        if (mana >=1){
            int damage = getBaseAttack()*2;
            mana = mana - 10;
            System.out.println(getName() + " стреляет магией в героя " + target.getName() + " (урон: " + damage + ", мана: -10)");
            target.takeDamage(damage);
        } else {
            int damage = getBaseAttack()/2;
            mana = mana + 5;
            if (mana > maxMana) {
                mana = maxMana;
            }
            System.out.println(getName() + " бьёт посохом героя " + target.getName() + " (урон: " + damage + ", мана: +5)");
            target.takeDamage(damage);
        }
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
        return ActionType.REST;
    }

    @Override
    public boolean canCast(){
        return mana >= 25;
    }

    @Override
    public void castSpecialSkill(Hero target){
        if (!canCast()){
            System.out.println(getName() + " хотел бросить огненную глыбу, но заклинание сорвалось: мало маны!");
            return;
        }
        int damage = getBaseAttack()*3;
        mana = mana - 25;
        System.out.println(getName() + " бросает огненную глыбу в героя " + target.getName() + " (урон: " + damage + "мана: -25)");
        target.takeDamage(damage);
    }

    @Override
    public boolean needsRest(){
        return mana == 0 || getHealth()*1 < getMaxHealth()*30;
    }

    @Override
    public void rest(){
        mana = maxMana;
        heal(getBaseAttack());
        System.out.println(getName() + " медитирует: мана полная, доровье +" + getBaseAttack());
    }

    @Override
    public String getStatus(){
        return super.getStatus() + ", мана " + mana + "/" + maxMana;
    }
}
