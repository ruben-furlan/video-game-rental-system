package com.videogamerentalsystem.infraestucture.adapter.out.persistence.inventory;


import com.videogamerentalsystem.common.PersistenceAdapter;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.port.out.inventory.GameInventoryRepositoryPort;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.mapper.inventory.GameInventoryMapper;
import com.videogamerentalsystem.infraestucture.adapter.out.repository.SpringDataJpaGameInventory;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class GameInventoryPersistenceAdapter implements GameInventoryRepositoryPort {
    private final SpringDataJpaGameInventory springDataJpaGameInventory;
    private final GameInventoryMapper gameInventoryMapper;

    @Override
    public GameInventoryModel save(GameInventoryModel gameInventoryModel) {
        GameInventoryEntity gameInventoryEntity = this.gameInventoryMapper.toEntityGameInventory(gameInventoryModel);
        GameInventoryEntity save = this.springDataJpaGameInventory.save(gameInventoryEntity);
        return this.gameInventoryMapper.toModelGameInventory(save);
    }

    @Override
    public Optional<GameInventoryModel> findByID(Long id) {
        return  this.springDataJpaGameInventory.findById(id).map(this.gameInventoryMapper::toModelGameInventory);
    }

    @Override
    public Optional<GameInventoryModel> findByProductId(Long productId) {
        return this.springDataJpaGameInventory.findFirstByInventoryPriceEntityId(productId).map(this.gameInventoryMapper::toModelGameInventory);
    }

    @Override
    public Optional<GameInventoryModel> findByTitle(String title) {
        return this.springDataJpaGameInventory.findFirstByTitle(title).map(this.gameInventoryMapper::toModelGameInventory);
    }

    @Override
    public void updateStock(Long id, Integer stock) {
        Optional<GameInventoryEntity> optionalGameInventoryEntity =this.springDataJpaGameInventory.findById(id);
        if (optionalGameInventoryEntity.isPresent()) {
            GameInventoryEntity gameInventoryEntity = optionalGameInventoryEntity.get();
            gameInventoryEntity.updateStock(stock);
            this.springDataJpaGameInventory.save(gameInventoryEntity);
        } else {
            throw new ApiException("Game inventory with id " + id + " not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void addStock(Long id) {

    }
}
