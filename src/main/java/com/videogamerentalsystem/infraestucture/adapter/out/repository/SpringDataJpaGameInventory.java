package com.videogamerentalsystem.infraestucture.adapter.out.repository;

import com.videogamerentalsystem.infraestucture.adapter.out.entity.inventory.GameInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaGameInventory  extends JpaRepository<GameInventoryEntity, Long> {
}
