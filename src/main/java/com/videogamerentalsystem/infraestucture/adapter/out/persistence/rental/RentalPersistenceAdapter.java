package com.videogamerentalsystem.infraestucture.adapter.out.persistence.rental;


import com.videogamerentalsystem.common.PersistenceAdapter;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.port.out.rental.RentalRepositoryPort;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.mapper.rental.RentalMapper;
import com.videogamerentalsystem.infraestucture.adapter.out.repository.SpringDataJpaRental;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class RentalPersistenceAdapter implements RentalRepositoryPort {

    private final SpringDataJpaRental springDataJpaRental;
    private final RentalMapper rentalMapper;

    @Override
    public RentalModel create(RentalModel rentalModel) {
        RentalEntity rentalEntity = this.rentalMapper.toRentalEntity(rentalModel);
        RentalEntity save = this.springDataJpaRental.save(rentalEntity);
        return this.rentalMapper.toRentalModel(save);
    }

    @Override
    public Optional<RentalProductModel> findGameByTitleAndRentalId(String tile, Long rentalId) {
       return this.springDataJpaRental.findRentalProductByTitleAndRentalId(tile, rentalId).map(this.rentalMapper::toRentalProductModel);
    }
}
