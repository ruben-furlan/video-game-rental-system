package com.videogamerentalsystem.domain.port.out.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import java.util.Optional;

public interface GameInventoryRepositoryPort {

    public GameInventoryModel save(GameInventoryModel gameInventoryModell);

    public Optional<GameInventoryModel> findByID(Long id);

}
