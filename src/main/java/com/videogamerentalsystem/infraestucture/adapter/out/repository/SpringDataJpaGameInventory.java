package com.videogamerentalsystem.infraestucture.adapter.out.repository;

import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaGameInventory  extends JpaRepository<GameInventoryEntity, Long> {

    List<GameInventoryEntity> findAllByTitleIn(List<String> titles);

    Optional<GameInventoryEntity> findFirstByTitle(String title);
}
