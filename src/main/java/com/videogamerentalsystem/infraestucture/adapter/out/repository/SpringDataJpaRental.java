package com.videogamerentalsystem.infraestucture.adapter.out.repository;

import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalProductEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaRental extends JpaRepository<RentalEntity, Long>  {
    @Query("SELECT rp FROM RentalProductEntity rp JOIN rp.rental r WHERE rp.title = :title AND r.id = :rentalId")
    Optional<RentalProductEntity> findRentalProductByTitleAndRentalId(String title, Long rentalId);

}
