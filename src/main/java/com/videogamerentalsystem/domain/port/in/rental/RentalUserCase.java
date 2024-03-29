package com.videogamerentalsystem.domain.port.in.rental;

import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCommand;

public interface RentalUserCase {

    public RentalModel create (RentalCommand rentalCommand);

    public RentalProductModel findGameByTitleAndRentalId(String title, Long rentalId);

}
