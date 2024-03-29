package com.videogamerentalsystem.domain.port.in.rental;

import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import java.util.Set;

public interface RentalLoyaltyUserCase {
    public Integer calculateTotalPoints(Set<RentalProductModel> productModels);
}
