package com.videogamerentalsystem.domain.port.out.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import java.util.List;
import java.util.Optional;

/**
 * Interface for the game inventory repository port.
 */
public interface GameInventoryRepositoryPort {

    /**
     * Saves the provided game inventory model.
     *
     * @param gameInventoryModel The game inventory model to save.
     * @return The saved game inventory model.
     */
    GameInventoryModel save(GameInventoryModel gameInventoryModel);

    /**
     * Finds the game inventory by its unique identifier.
     *
     * @param id The identifier of the game inventory to find.
     * @return An optional containing the game inventory model if found, otherwise empty.
     */
    Optional<GameInventoryModel> findByID(Long id);

    /**
     * Finds the game inventory by its title.
     *
     * @param title The title of the game inventory to find.
     * @return An optional containing the game inventory model if found, otherwise empty.
     */
    Optional<GameInventoryModel> findByTitle(String title);


    /**
     * Finds the game inventory by its title.
     *
     * @param title The title of the game inventory to find.
     * @return An optional containing the game inventory model if found, otherwise empty.
     */
    List<GameInventoryModel> findByTitles(List<String> title);


    /**
     * Updates the stock quantity for the specified game inventory.
     *
     * @param id    The identifier of the game inventory.
     * @param stock The new stock quantity to set.
     */
    void updateStock(Long id, Integer stock);

}