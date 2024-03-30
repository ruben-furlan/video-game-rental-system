package com.videogamerentalsystem.domain.port.out.rental;

import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Interface for the rental repository port.
 */
public interface RentalRepositoryPort {

    /**
     * Creates a new rental with the provided rental model.
     *
     * @param rentalModel The rental model to create.
     * @return The created rental model.
     */
    RentalModel create(RentalModel rentalModel);

    /**
     * Finds a rental by its unique identifier.
     *
     * @param id The identifier of the rental to find.
     * @return An optional containing the rental model if found, otherwise empty.
     */
    Optional<RentalModel> findRentalById(Long id);

    /**
     * Updates the status and price of a rental product within a rental.
     *
     * @param rentalId    The identifier of the rental.
     * @param productId   The identifier of the rental product.
     * @param price       The new price to set for the rental product.
     * @param status      The new status to set for the rental product.
     */
    void updateStatusProductAndPrice(Long rentalId, Long productId, BigDecimal price, RentalProductStatus status);
}