package com.videogamerentalsystem.domain.model.rental.constant;

public enum RentalProductStatus {
    IN_PROGRESS, FINISH;

    public boolean isFinish() {
        return FINISH.equals(this);
    }

}
