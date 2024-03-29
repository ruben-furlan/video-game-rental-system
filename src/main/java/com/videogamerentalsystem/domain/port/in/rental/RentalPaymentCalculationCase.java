package com.videogamerentalsystem.domain.port.in.rental;

import com.videogamerentalsystem.domain.model.rental.RentalModel;

public interface RentalPaymentCalculationCase {
    public void calculateAndSetRentalCost(RentalModel rentalModel);
}
