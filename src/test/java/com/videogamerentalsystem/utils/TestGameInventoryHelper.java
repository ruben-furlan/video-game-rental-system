package com.videogamerentalsystem.utils;

import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import com.videogamerentalsystem.domain.model.inventory.constant.GamePriceType;
import com.videogamerentalsystem.domain.model.inventory.constant.GameType;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryCommand;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryPriceCommand;
import java.math.BigDecimal;

public abstract class TestGameInventoryHelper {

    public static final String TITLE_GAME_NEW_RELEASE = "No Mans Sky";
    public static final String TITLE_GAME_STANDARD = "Resident Evil 6";
    public static final String TITLE_GAME_CLASSIC = "Fallout 3";

    public static final String TITLE_GAME_CLASSIC_2 = "Fallout 4";
    public static final String NO_MANS_SKY = "No Mans Sky";
    public static final String DEFAULT_CURRENCY = "EUR";


    public static GameInventoryCommand generateNewReleaseGameInventoryPriceDefaultCommand() {
        return new GameInventoryCommand(NO_MANS_SKY, GameType.NEW_RELEASE, 10, new GameInventoryPriceCommand(GamePriceType.PREMIUM, BigDecimal.valueOf(4), DEFAULT_CURRENCY));
    }

    public static GameInventoryModel generateNewReleaseGameInventoryModelDefault() {
        return GameInventoryModel.builder()
                .title(NO_MANS_SKY)
                .type(GameType.NEW_RELEASE)
                .stock(10)
                .inventoryPriceModel(GameInventoryPriceModel.builder().type(GamePriceType.PREMIUM).currency(DEFAULT_CURRENCY).amount(BigDecimal.valueOf(4)).build())
                .build();
    }

    public static GameInventoryModel generateStandardGameInventoryModelDefault() {
        return GameInventoryModel.builder()
                .title(TITLE_GAME_STANDARD)
                .type(GameType.STANDARD)
                .stock(10)
                .inventoryPriceModel(GameInventoryPriceModel.builder().type(GamePriceType.BASIC).currency(DEFAULT_CURRENCY).amount(BigDecimal.valueOf(4)).build())
                .build();
    }

    public static GameInventoryModel generateClassicGameInventoryModelDefault() {
        return GameInventoryModel.builder()
                .title(TITLE_GAME_CLASSIC)
                .type(GameType.CLASSIC)
                .stock(5)
                .inventoryPriceModel(GameInventoryPriceModel.builder().type(GamePriceType.BASIC).currency(DEFAULT_CURRENCY).amount(BigDecimal.valueOf(4)).build())
                .build();
    }

    public static GameInventoryModel generateClassicGameInventory(Integer stock) {
        return GameInventoryModel.builder()
                .title(TITLE_GAME_CLASSIC)
                .type(GameType.CLASSIC)
                .stock(stock)
                .inventoryPriceModel(GameInventoryPriceModel.builder().type(GamePriceType.BASIC).currency(DEFAULT_CURRENCY).amount(BigDecimal.valueOf(4)).build())
                .build();
    }

}
