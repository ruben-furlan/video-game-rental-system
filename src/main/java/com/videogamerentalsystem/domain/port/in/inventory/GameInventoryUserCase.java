package com.videogamerentalsystem.domain.port.in.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryCommand;
import java.util.Optional;
import java.util.Set;

public interface GameInventoryUserCase {

    public GameInventoryModel create(GameInventoryCommand gameInventoryCommand);

    public Optional<GameInventoryModel> findInventoryById(Long id);

    public Optional<GameInventoryModel> findInventoryByTitle(String title);

    public Set<GameInventoryModel> stockExists(Set<RentalProductModel> productModels);

    public void stockRemove(Set<GameInventoryModel> gameInventoryModels);

    public void stackAdd(Set<GameInventoryModel> gameInventoryModels);

}
