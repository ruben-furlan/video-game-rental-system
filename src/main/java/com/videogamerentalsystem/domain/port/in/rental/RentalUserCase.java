package com.videogamerentalsystem.domain.port.in.rental;

import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCommand;
/**
 * Interface for the rental use case.
 */
public interface RentalUserCase {

    /**
     * Creates a rental based on the provided rental command.
     *
     * @param rentalCommand The rental command containing rental details.
     * @return The created rental model.
     */
    RentalModel create(RentalCommand rentalCommand);

    /**
     * Retrieves a rental by its ID.
     *
     * @param rentalId The ID of the rental to retrieve.
     * @return The rental model corresponding to the given ID.
     */
    RentalModel get(Long rentalId);

    /**
     * Handles the return of a game associated with a rental.
     *
     * @param rentalId The ID of the rental.
     * @param rentalProductId The ID of the rentalProductId being returned.
     * @return The updated rental model after the game return.
     */
    RentalModel handBackGame(Long rentalId, Long rentalProductId);
}