package com.videogamerentalsystem.utils;

import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.model.rental.RentalCustomerModel;
import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductChargeModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductSurchargeModel;
import com.videogamerentalsystem.domain.model.rental.constant.RentalPaymentType;
import com.videogamerentalsystem.domain.model.rental.constant.RentalProductStatus;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCustomerCommand;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalProductCommand;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestRentalHelper {
    public final class Customer {
        public static final String FIRST_NAME = "LEO";
        public static final String LAST_NAME = "MESSI";
        public static final String DOCUMENT_NUMBER = "BEST-10";
    }

    public static RentalCommand generateRentalDefaultCommand() {
        return generateRentalCommand(TestGameInventoryHelper.DEFAULT_CURRENCY,RentalPaymentType.CREDIT);
    }

    public static RentalCommand generateRentalCommand(String currency, RentalPaymentType paymentType) {
        RentalCustomerCommand customerCommand = new RentalCustomerCommand(Customer.FIRST_NAME, Customer.LAST_NAME, Customer.DOCUMENT_NUMBER);

        List<RentalProductCommand> rentalProductCommands = Arrays.asList(
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")),
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_STANDARD, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")),
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")),
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_CLASSIC_2, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")));

        return new RentalCommand(TestGameInventoryHelper.DEFAULT_CURRENCY, paymentType, customerCommand, rentalProductCommands);
    }

    public static RentalCommand generateRentalCommandDuplicateProduct(String currency, RentalPaymentType paymentType) {
        RentalCustomerCommand customerCommand = new RentalCustomerCommand(Customer.FIRST_NAME, Customer.LAST_NAME, Customer.DOCUMENT_NUMBER);

        List<RentalProductCommand> rentalProductCommands = Arrays.asList(
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")),
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")),
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_STANDARD, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")),
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_STANDARD, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")),
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")),
                generateRentalProductCommand(TestGameInventoryHelper.TITLE_GAME_CLASSIC_2, DateFormatter.toLocalDateTime("2024-04-04T23:59:59")));

        return new RentalCommand(TestGameInventoryHelper.DEFAULT_CURRENCY, paymentType, customerCommand, rentalProductCommands);
    }



    public static RentalProductCommand generateRentalProductCommand(String title, LocalDateTime endDate) {
        return new RentalProductCommand(title, endDate);
    }


    public static RentalModel generateRentalDefaultMode() {
        RentalCustomerModel rentalCustomerModel = generateRentalCustomerModel(10);
        List<RentalProductModel> rentalProductModels = Arrays.asList(
                generateRentalProductGameNewRelease(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, GameType.NEW_RELEASE, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(20.00)),
                generateRentalProductGameNewRelease(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_STANDARD, GameType.STANDARD, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(6.00)),
                generateRentalProductGameNewRelease(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_CLASSIC, GameType.CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(6.00)),
                generateRentalProductGameNewRelease(RentalProductStatus.IN_PROGRESS, TestGameInventoryHelper.TITLE_GAME_CLASSIC_2, GameType.CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(3.00))
        );

        return RentalModel.builder()
                .id(1L)
                .date(DateFormatter.toLocalDateTime("2024-03-31T00:00:00"))
                .currency(TestGameInventoryHelper.DEFAULT_CURRENCY)
                .paymentType(RentalPaymentType.CREDIT)
                .customerModel(rentalCustomerModel)
                .productModels(rentalProductModels).build();
    }

    public static RentalModel generateRentalDefaultModeProductFinish() {
        RentalCustomerModel rentalCustomerModel = generateRentalCustomerModel(10);
        List<RentalProductModel> rentalProductModels = Arrays.asList(
                generateRentalProductGameNewRelease(RentalProductStatus.FINISH, TestGameInventoryHelper.TITLE_GAME_NEW_RELEASE, GameType.NEW_RELEASE, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(20.00)),
                generateRentalProductGameNewRelease(RentalProductStatus.FINISH, TestGameInventoryHelper.TITLE_GAME_STANDARD, GameType.STANDARD, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(6.00)),
                generateRentalProductGameNewRelease(RentalProductStatus.FINISH, TestGameInventoryHelper.TITLE_GAME_CLASSIC, GameType.CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(6.00)),
                generateRentalProductGameNewRelease(RentalProductStatus.FINISH, TestGameInventoryHelper.TITLE_GAME_CLASSIC_2, GameType.CLASSIC, DateFormatter.toLocalDateTime("2024-04-04T23:59:59"), BigDecimal.valueOf(3.00))
        );

        return RentalModel.builder()
                .id(1L)
                .date(DateFormatter.toLocalDateTime("2024-03-31T00:00:00"))
                .currency(TestGameInventoryHelper.DEFAULT_CURRENCY)
                .paymentType(RentalPaymentType.CREDIT)
                .customerModel(rentalCustomerModel)
                .productModels(rentalProductModels).build();
    }

    public static RentalCustomerModel generateRentalCustomerModel(Integer loyaltyPoint) {
        return RentalCustomerModel.builder().firstName(Customer.FIRST_NAME).latName(Customer.LAST_NAME).documentNumber(Customer.DOCUMENT_NUMBER).loyaltyPoints(loyaltyPoint).build();
    }

    public static RentalProductModel generateRentalProductGameNewRelease(RentalProductStatus status, String title, GameType type, LocalDateTime endDate, BigDecimal price) {
        return RentalProductModel.builder()
                .id(1L)
                .status(status)
                .title(title)
                .type(type)
                .endDate(endDate)
                .charges(generateChargeModel(price))
                .build();
    }

    public static RentalProductChargeModel generateChargeModel(BigDecimal price) {
        return generateChargeModel(price, null, null);
    }

    public static RentalProductChargeModel generateChargeModel(BigDecimal price, BigDecimal total, RentalProductSurchargeModel surchargeModel) {
        RentalProductChargeModel.RentalProductChargeModelBuilder builder = RentalProductChargeModel.builder();
        if (Objects.nonNull(surchargeModel)) {
            builder.surcharges(surchargeModel);
            builder.total(total);
        }
        return builder.price(price).build();
    }
}
