package com.videogamerentalsystem.domain.port.in.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryCommand;
import java.util.List;
import java.util.Optional;


/**
 * Interface for the game inventory use case.
 */
public interface GameInventoryUserCase {

    /**
     * Creates a new game inventory based on the provided command.
     *
     * @param gameInventoryCommand The command containing the details for creating the game inventory.
     * @return The created game inventory model.
     */
    GameInventoryModel create(GameInventoryCommand gameInventoryCommand);

    /**
     * Finds the game inventory by its unique identifier.
     *
     * @param id The identifier of the game inventory to find.
     * @return An optional containing the game inventory model if found, otherwise empty.
     */
    Optional<GameInventoryModel> findInventoryById(Long id);


    /**
     * Finds a game inventory by its title.
     *
     * @param title The title of the game inventory to find.
     * @return An Optional containing the game inventory if found, or empty if not found.
     */
    Optional<GameInventoryModel> findInventoryByTitle(String title);

    /**
     * Checks if the stock exists for the provided set of rental product models.
     *
     * @param productModels The set of rental product models to check stock for.
     * @return A list of game inventory models representing the available stock.
     */
    List<GameInventoryModel> stockExists(List<String> titles);

    /**
     * Removes stock from the game inventory for the provided set of game inventory models.
     *
     * @param gameInventoryModels The List of game inventory models for which to remove stock.
     */
    void stockRemove(List<GameInventoryModel> gameInventoryModels);

    /**
     * Add stock to the game inventory for the provided set of game inventory model.
     *
     * @param gameInventoryModel game inventory models for which to add stock.
     */
    void stockAdd(GameInventoryModel gameInventoryModels);
}
