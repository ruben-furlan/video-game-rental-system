package com.videogamerentalsystem.infraestucture.adapter.out.persistence.rental;


import com.videogamerentalsystem.common.PersistenceAdapter;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import com.videogamerentalsystem.domain.port.out.rental.RentalRepositoryPort;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.entity.rental.RentalProductEntity;
import com.videogamerentalsystem.infraestucture.adapter.out.mapper.rental.RentalMapper;
import com.videogamerentalsystem.infraestucture.adapter.out.repository.SpringDataJpaRental;
import java.math.BigDecimal;
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
    public Optional<RentalModel> findRentalById(Long id) {
        Optional<RentalEntity> findRentalEntityById = this.springDataJpaRental.findById(id);
        return findRentalEntityById.map(this.rentalMapper::toRentalModel);
    }

    @Override
    public void updateStatusProductAndPrice(Long rentalId, Long productId, BigDecimal price, RentalProductStatus status) {
        Optional<RentalEntity> rentalEntity = this.springDataJpaRental.findById(rentalId);
        rentalEntity.ifPresent(rental -> {
            this.finRentalProductById(productId, rental).ifPresent(rentalProduct -> rentalProduct.updatePriceAndStatus(price, status));
            this.springDataJpaRental.save(rental);
        });
    }

    private Optional<RentalProductEntity> finRentalProductById(Long productId, RentalEntity rental) {
        return rental.getRentalProducts()
                .stream()
                .filter(game -> game.getId().equals(productId))
                .findFirst();
    }

}
