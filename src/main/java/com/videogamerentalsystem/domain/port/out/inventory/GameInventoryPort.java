package com.videogamerentalsystem.domain.port.out.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import java.util.List;
import java.util.Optional;

public interface GameInventoryPort {

    public GameInventoryModel save(GameInventoryPriceModel gameInventoryPrice);

    public Optional<List<GameInventoryPriceModel>> findInventoryPriceById(String id);

}
