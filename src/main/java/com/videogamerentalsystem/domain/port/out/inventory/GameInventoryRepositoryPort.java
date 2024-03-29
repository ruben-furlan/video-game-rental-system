package com.videogamerentalsystem.domain.port.out.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import java.util.Optional;

public interface GameInventoryRepositoryPort {

    public GameInventoryModel save(GameInventoryModel gameInventoryModell);

    public Optional<GameInventoryModel> findByID(Long id);

    public Optional<GameInventoryModel> findByTitle(String title);

    public void removeStock(Long id);

    public void addStock(Long id) ;

}
