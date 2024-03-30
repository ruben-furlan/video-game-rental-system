package com.videogamerentalsystem.domain.port.in.rental;

import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import java.util.List;


/**
 * Interface for the rental loyalty calculation use case.
 */
public interface RentalLoyaltyUserCase {

    /**
     * Calculates the total loyalty points based on the provided set of rental product models.
     *
     * @param productModels The List of rental product models for which to calculate loyalty points.
     * @return The total loyalty points calculated.
     */
    Integer calculateTotalPoints(List<RentalProductModel> productModels);
}