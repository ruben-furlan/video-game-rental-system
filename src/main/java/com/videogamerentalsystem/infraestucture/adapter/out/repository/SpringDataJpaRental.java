package com.videogamerentalsystem.infraestucture.adapter.out.repository;

import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaRental extends JpaRepository<RentalEntity, Long>  {

}
