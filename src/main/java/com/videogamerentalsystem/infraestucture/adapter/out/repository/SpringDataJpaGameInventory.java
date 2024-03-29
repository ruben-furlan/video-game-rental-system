package com.videogamerentalsystem.infraestucture.adapter.out.repository;

import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaGameInventory  extends JpaRepository<GameInventoryEntity, Long> {
    Optional<GameInventoryEntity> findFirstByTitle(String title);

    Set<GameInventoryEntity> findAllByIdIn(List<Long> ids);
}
