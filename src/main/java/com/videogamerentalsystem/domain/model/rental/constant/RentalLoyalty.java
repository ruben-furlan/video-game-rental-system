package com.videogamerentalsystem.domain.model.rental.constant;

public enum RentalLoyalty {

    NEW_RELEASE_POINT(2), STANDARD_POINT(1), CLASSIC_POINT(1);

    private final int value;

    RentalLoyalty(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
