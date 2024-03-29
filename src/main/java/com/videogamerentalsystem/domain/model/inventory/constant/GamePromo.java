package com.videogamerentalsystem.domain.model.inventory.constant;


public enum GamePromo {
    STANDARD(3), CLASSIC(5);

    private final int value;

    GamePromo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}