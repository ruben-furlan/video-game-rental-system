package com.videogamerentalsystem.domain.port.in.rental;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import java.util.Set;

public interface RentalPaymentCalculationCase {
    public void calculateAndSetRentalCost(RentalModel rentalModel, Set<GameInventoryModel> gameInventoryModel);
}
