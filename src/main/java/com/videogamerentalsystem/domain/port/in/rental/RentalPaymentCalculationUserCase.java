package com.videogamerentalsystem.domain.port.in.rental;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import java.util.List;


/**
 * Interface for the rental payment calculation use case.
 */
public interface RentalPaymentCalculationUserCase {

    /**
     * Applies and calculates the rental cost based on the provided rental model and game inventory models.
     *
     * @param rentalModel The rental model containing rental details.
     * @param gameInventoryModel The List of game inventory models associated with the rental.
     */
    void applyAndCalculateRentalCost(RentalModel rentalModel, List<GameInventoryModel> gameInventoryModel);

    /**
     * Applies a surcharge for the given rental product model.
     *
     * @param productModel The rental product model for which to apply the surcharge.
     */
    void applySurchargeForProduct(RentalProductModel productModel);
}