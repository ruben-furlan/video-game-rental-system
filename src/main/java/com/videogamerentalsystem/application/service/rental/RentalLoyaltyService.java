package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalLoyalty;
import com.videogamerentalsystem.domain.port.in.rental.RentalLoyaltyUserCase;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


@UseCase
public class RentalLoyaltyService implements RentalLoyaltyUserCase {
    private static final Map<GameType, RentalLoyalty> POINTS_PER_TYPE_CONF = Map.of(
            GameType.NEW_RELEASE, RentalLoyalty.NEW_RELEASE_POINT,
            GameType.STANDARD, RentalLoyalty.STANDARD_POINT,
            GameType.CLASSIC, RentalLoyalty.CLASSIC_POINT
    );

    public Integer calculateTotalPoints(List<RentalProductModel> productModels) {
        return productModels.stream()
                .map(product -> POINTS_PER_TYPE_CONF.getOrDefault(product.getType(), RentalLoyalty.CLASSIC_POINT).getValue())
                .reduce(0, Integer::sum);
    }

}
