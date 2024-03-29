package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.constant.GamePromo;
import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductChargeModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.port.in.rental.RentalPaymentCalculationCase;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiException;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiExceptionConstantsMessagesError;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@UseCase
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class RentalPaymentCalculationService implements RentalPaymentCalculationCase {

    @Override
    public void calculateAndSetRentalCost(RentalModel rentalModel, Set<GameInventoryModel> gameInventoryModels) {
        if (Objects.isNull(rentalModel)|| CollectionUtils.isEmpty(rentalModel.getProductModels())) {
            throw new ApiException(ApiExceptionConstantsMessagesError.MESSAGE_GENERIC, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        rentalModel.getProductModels().forEach(productModel -> this.applyCostConditions(rentalModel, gameInventoryModels, productModel));
    }

    private void applyCostConditions (RentalModel rentalModel, Set<GameInventoryModel> gameInventoryModels, RentalProductModel productModel) {
        GameInventoryModel gameInventoryModel = this.findGameInventoryModel(gameInventoryModels, productModel);

        Integer numberDays = this.calculateNumberDays(productModel, rentalModel.getDate());

        BigDecimal gameInventoryPriceAmount = gameInventoryModel.getInventoryPriceModel().getAmount();

        BigDecimal rentalProductCharge = this.getChargeToApplyByGameType(numberDays, gameInventoryModel.getType(), gameInventoryPriceAmount);

        productModel.setCharges(RentalProductChargeModel.builder().price(rentalProductCharge).build());

        productModel.setNumberDays(numberDays);
    }

    private GameInventoryModel findGameInventoryModel(Set<GameInventoryModel> gameInventoryModels, RentalProductModel productModel) {
        return gameInventoryModels.stream().filter(game -> game.getTitle().equals(productModel.getTitle())).findFirst().orElse(null);
    }


    private BigDecimal getChargeToApplyByGameType(Integer numberDays, GameType gameType, BigDecimal priceAmount) {
        return switch (gameType) {
            case NEW_RELEASE -> this.getPriceWithOutPromo(numberDays, priceAmount);
            case STANDARD -> this.getPriceAmountWithPromo(numberDays, priceAmount, GamePromo.STANDARD.getValue());
            case CLASSIC -> this.getPriceAmountWithPromo(numberDays, priceAmount, GamePromo.CLASSIC.getValue());
        };
    }

    private BigDecimal getPriceWithOutPromo(Integer numberDays, BigDecimal priceAmount) {
        return priceAmount.multiply(BigDecimal.valueOf(numberDays));
    }

    private BigDecimal getPriceAmountWithPromo(Integer numberDays, BigDecimal amount, Integer promoDateGame) {
        long extraDays = Math.max(0, numberDays - promoDateGame);
        if (extraDays > 0) {
            BigDecimal multiply = amount.multiply(BigDecimal.valueOf(extraDays));
            return multiply.add(amount);
        }
        return amount;
    }

    private Integer calculateNumberDays(RentalProductModel productModel, LocalDateTime date) {
        long dayDiff = ChronoUnit.DAYS.between(date.toLocalDate(), productModel.getEndDate().toLocalDate());
        if (dayDiff == 0) {
            return 1;
        }
        return Math.toIntExact(dayDiff);
    }

}
