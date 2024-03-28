package com.videogamerentalsystem.domain.port.out.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import java.util.List;
import java.util.Optional;

public interface GameInventoryPort {

    public GameInventoryModel save(GameInventoryModel gameInventoryModell);

    public Optional<GameInventoryModel> findByID(Long id);

}
