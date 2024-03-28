package com.videogamerentalsystem.domain.port.in.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryCommand;
import java.util.Optional;

public interface GameInventoryUserCase {

    public GameInventoryModel create(GameInventoryCommand gameInventoryCommand);

    public Optional<GameInventoryModel> findInventoryById(Long id);

}
