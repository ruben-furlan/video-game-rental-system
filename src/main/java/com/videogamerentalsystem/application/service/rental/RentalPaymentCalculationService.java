package com.videogamerentalsystem.application.service.rental;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.constant.GamePromo;
import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductChargeModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductSurchargeModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import com.videogamerentalsystem.domain.port.in.rental.RentalPaymentCalculationUserCase;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiException;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiExceptionConstantsMessagesError;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@UseCase
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class RentalPaymentCalculationService implements RentalPaymentCalculationUserCase {

    public static final String EXTRAS_DAYS = "EXTRAS_DAYS";

    @Override
    public void applyAndCalculateRentalCost(RentalModel rentalModel, List<GameInventoryModel> gameInventoryModels) {
        if (Objects.isNull(rentalModel) || CollectionUtils.isEmpty(rentalModel.getProductModels())) {
            throw new ApiException(ApiExceptionConstantsMessagesError.Generic.MESSAGE_GENERIC, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        rentalModel.getProductModels().forEach(productModel -> this.applyCostConditions(rentalModel, gameInventoryModels, productModel));
    }

    @Override
    public void applySurchargeForProduct(RentalProductModel productModel) {
        if (Objects.isNull(productModel)) {
            throw new ApiException(ApiExceptionConstantsMessagesError.Generic.MESSAGE_GENERIC, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Integer numberDays = this.calculateNumberDays(productModel, LocalDateTime.now());
        if (this.isDelayed(numberDays)) {
            numberDays = Math.abs(numberDays);
            RentalProductChargeModel charges = productModel.getCharges();
            BigDecimal chargesPrice = charges.getPrice();
            BigDecimal total = this.getTotal(numberDays, chargesPrice);
            BigDecimal priceWithOutPromo = this.getPriceWithOutPromo(numberDays, chargesPrice);
            BigDecimal subTotal = priceWithOutPromo.subtract(chargesPrice);
            productModel.getCharges().updateTotalAndPrice(total, chargesPrice);
            charges.addSurchargeModel(this.buildSurcharge(subTotal));
        }
    }

    private void applyCostConditions(RentalModel rentalModel, List<GameInventoryModel> gameInventoryModels, RentalProductModel productModel) {
        GameInventoryModel gameInventoryModel = this.findGameInventoryModel(gameInventoryModels, productModel);
        if (Objects.nonNull(gameInventoryModel)) {
            productModel.updateStatus(RentalProductStatus.IN_PROGRESS);
            productModel.updateType(gameInventoryModel.getType());
            Integer numberDays = this.calculateNumberDays(productModel, rentalModel.getDate());
            BigDecimal gameInventoryPriceAmount = gameInventoryModel.getInventoryPriceModel().getAmount();
            BigDecimal rentalProductCharge = this.getChargeToApplyByGameType(numberDays, gameInventoryModel.getType(), gameInventoryPriceAmount);
            productModel.updateCharges(RentalProductChargeModel.builder().price(rentalProductCharge).build());
            productModel.updateNumberDays(numberDays);
        }
    }

    private GameInventoryModel findGameInventoryModel(List<GameInventoryModel> gameInventoryModels, RentalProductModel productModel) {
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
        return (dayDiff == 0) ? 1 : Math.toIntExact(dayDiff);
    }
    private RentalProductSurchargeModel buildSurcharge(BigDecimal subTotal) {
        return RentalProductSurchargeModel.builder().amount(subTotal).reason(EXTRAS_DAYS).build();
    }
    private BigDecimal getTotal(Integer numberDays, BigDecimal price) {
        return (numberDays == 1) ? price.add(price) : this.getPriceWithOutPromo(numberDays, price);
    }
    private boolean isDelayed(Integer numberDays) {
        return numberDays < 0;
    }


}
