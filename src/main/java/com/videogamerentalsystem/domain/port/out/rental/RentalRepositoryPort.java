package com.videogamerentalsystem.domain.port.out.rental;

import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import java.util.Optional;

public interface RentalRepositoryPort {

    public RentalModel create(RentalModel rentalModel);

    public Optional<RentalProductModel> findGameByTitleAndRentalId(String tile, Long rentalId);

}
