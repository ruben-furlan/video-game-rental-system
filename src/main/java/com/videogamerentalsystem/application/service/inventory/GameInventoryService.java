package com.videogamerentalsystem.application.service.inventory;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import com.videogamerentalsystem.domain.port.in.inventory.GameInventoryUserCase;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryCommand;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryPriceCommand;
import com.videogamerentalsystem.domain.port.out.inventory.GameInventoryRepositoryPort;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiException;
import com.videogamerentalsystem.infraestucture.exception.custom.ApiExceptionConstantsMessagesError;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class GameInventoryService implements GameInventoryUserCase {

    private final GameInventoryRepositoryPort gameInventoryRepositoryPort;

    @Override
    public GameInventoryModel create(GameInventoryCommand gameInventoryCommand) {
        GameInventoryModel gameInventoryModel = this.buildToModelAndValidate(gameInventoryCommand);
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

    public List<GameInventoryModel> stockExists(List<String> titles) {
        List<GameInventoryModel> gameInventoryModels = this.gameInventoryRepositoryPort.findByTitles(titles);
        if (this.allMatchStock(titles,gameInventoryModels)) {
            return gameInventoryModels;
        }
        throw new ApiException(ApiExceptionConstantsMessagesError.NOT_STOCK, HttpStatus.BAD_REQUEST);

    }

    @Override
    public void stockRemove(List<GameInventoryModel> gameInventoryModels) {
        gameInventoryModels.forEach(gameInventoryModelCurrent -> {
            Integer newStock = gameInventoryModelCurrent.getStock() - 1;
            if (newStock >= 0) {
                this.gameInventoryRepositoryPort.updateStock(gameInventoryModelCurrent.getId(), newStock);
            }
        });
    }

    @Override
    public void stockAdd(GameInventoryModel gameInventoryModel) {
        int newStock = gameInventoryModel.getStock() + 1;
        this.gameInventoryRepositoryPort.updateStock(gameInventoryModel.getId(), newStock);
    }


    private GameInventoryModel buildToModelAndValidate(GameInventoryCommand gameInventoryCommand) {
        String title = gameInventoryCommand.title();
        this.gameInventoryRepositoryPort.findByTitle(title)
                .ifPresent(game -> {
                    String errorMessage = "The title '%s' is already present. Please check the inventory, as it may not have enough stock.".formatted(title);
                    throw new ApiException(errorMessage, HttpStatus.BAD_REQUEST);
                });

        GameInventoryPriceCommand gameInventoryPriceCommand = gameInventoryCommand.price();

        GameInventoryPriceModel gameInventoryPriceModel = GameInventoryPriceModel.builder()
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

    private boolean allMatchStock(List<String> titles, List<GameInventoryModel> gameInventoryModels) {
        // Verificar si todos los títulos están presentes en los modelos de inventario
        boolean allTitlesPresent = titles.stream()
                .allMatch(title -> gameInventoryModels.stream()
                        .anyMatch(model -> model.getTitle().equals(title)));

        if (!allTitlesPresent) {
            return false; // Si no todos los títulos están presentes, devolver falso
        }

        // Verificar si hay suficiente stock para cada título
        boolean allStockAvailable = titles.stream()
                .allMatch(title -> gameInventoryModels.stream()
                        .filter(model -> model.getTitle().equals(title))
                        .mapToLong(GameInventoryModel::getStock)
                        .sum() > 0);

        return allStockAvailable; // Devolver verdadero si hay suficiente stock para todos los títulos
    }

}
