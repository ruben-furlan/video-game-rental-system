package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalLoyalty;
import com.videogamerentalsystem.domain.port.in.rental.RentalLoyaltyUserCase;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@UseCase
public class RentalLoyaltyService implements RentalLoyaltyUserCase {
    private static final Map<GameType, RentalLoyalty> POINTS_PER_TYPE = new EnumMap<>(GameType.class);

    static {
        POINTS_PER_TYPE.put(GameType.NEW_RELEASE, RentalLoyalty.NEW_RELEASE_POINT);
        POINTS_PER_TYPE.put(GameType.STANDARD, RentalLoyalty.STANDARD_POINT);
        POINTS_PER_TYPE.put(GameType.CLASSIC, RentalLoyalty.CLASSIC_POINT);
    }

    public Integer calculateTotalPoints(Set<RentalProductModel> productModels) {
        return productModels.stream()
                .mapToInt(product -> POINTS_PER_TYPE.getOrDefault(product.getType(), RentalLoyalty.CLASSIC_POINT).getValue())
                .sum();
    }


}
