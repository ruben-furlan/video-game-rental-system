package com.videogamerentalsystem.domain.port.out.inventory;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GameInventoryRepositoryPort {

    public GameInventoryModel save(GameInventoryModel gameInventoryModel);

    public Optional<GameInventoryModel> findByID(Long id);

    public Set<GameInventoryEntity> findByIDs(List<Long> ids);

    public Optional<GameInventoryModel> findByTitle(String title);

    public void updateStock(Long id, Integer stock);

    public void addStock(Long id) ;

}
