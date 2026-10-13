package org.example;

public enum ActionType {
    BASE_ATTACK("Базовая атака"),
    SPECIAL_SKILL("особое умение"),
    REST("Отдых");

    private final String title;

    ActionType(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
