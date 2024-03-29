package com.videogamerentalsystem.application.service.inventory;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import com.videogamerentalsystem.domain.model.rental.RentalProductModel;
import com.videogamerentalsystem.domain.port.in.inventory.GameInventoryUserCase;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryCommand;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryPriceCommand;
import com.videogamerentalsystem.domain.port.out.inventory.GameInventoryRepositoryPort;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class GameInventoryService implements GameInventoryUserCase {

    private final GameInventoryRepositoryPort gameInventoryRepositoryPort;

    @Override
    public GameInventoryModel create(GameInventoryCommand gameInventoryCommand) {
        GameInventoryModel gameInventoryModel = this.buildToModel(gameInventoryCommand);
        return this.gameInventoryRepositoryPort.save(gameInventoryModel);
    }

    @Override
    public Optional<GameInventoryModel> findInventoryById(Long id) {
        return this.gameInventoryRepositoryPort.findByID(id);
    }

    @Override
    public Optional<GameInventoryModel> findInventoryByTitle(String title) {
        return this.gameInventoryRepositoryPort.findByTitle(title);
    }

    public Set<GameInventoryModel> stockExists(Set<RentalProductModel> productModels) {
        Set<GameInventoryModel> gameInventoryModels = this.findGameInventoryModelsByTitles(productModels);
        if (this.allMatchStock(gameInventoryModels)) {
            return gameInventoryModels;
        } else {
            return this.isNoStockForAnyProductEmptyList();
        }
    }


    @Override
    public void stockRemove(Set<GameInventoryModel> gameInventoryModels) {

    }

    @Override
    public void stackAdd(Set<GameInventoryModel> gameInventoryModels) {

    }


    private GameInventoryModel  buildToModel(GameInventoryCommand gameInventoryCommand) {

        GameInventoryPriceCommand gameInventoryPriceCommand = gameInventoryCommand.price();

        GameInventoryPriceModel gameInventoryPriceModel= GameInventoryPriceModel.builder()
                .type(gameInventoryPriceCommand.type())
                .currency(gameInventoryPriceCommand.currency())
                .amount(gameInventoryPriceCommand.amount())
                .build();

        return GameInventoryModel.builder()
                .title(gameInventoryCommand.title())
                .type(gameInventoryCommand.type())
                .stock(gameInventoryCommand.stock())
                .inventoryPriceModel(gameInventoryPriceModel).build();
    }


    private  Set<GameInventoryModel> isNoStockForAnyProductEmptyList() {
        return Collections.emptySet();
    }

    private  boolean allMatchStock(Set<GameInventoryModel> gameInventoryModels) {
        return !gameInventoryModels.isEmpty() && gameInventoryModels.stream().allMatch(game -> Objects.nonNull(game) && game.getStock() > 0);
    }

    private Set<GameInventoryModel> findGameInventoryModelsByTitles(Set<RentalProductModel> productModels) {
        return productModels.stream()
                .map(productModel -> this.gameInventoryRepositoryPort.findByTitle(productModel.getTitle()).orElse(null))
                .collect(Collectors.toSet());
    }


}
