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
import java.util.List;
import java.util.Optional;
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

    @Override
    public List<GameInventoryModel> stockExists(List<String> inputTitles) {
        List<GameInventoryModel> gameInventoryModels = this.gameInventoryRepositoryPort.findByTitles(inputTitles);
        if (this.allMatchStock(inputTitles, gameInventoryModels)) {
            return gameInventoryModels;
        }
        throw new ApiException(ApiExceptionConstantsMessagesError.GameInventory.NOT_STOCK, HttpStatus.BAD_REQUEST);
    }

    @Override
    public void stockRemove(List<GameInventoryModel> gameInventoryModels) {
        gameInventoryModels.forEach(gameInventoryModelCurrent -> {
            int newStock = gameInventoryModelCurrent.getStock() - 1;
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
        this.gameInventoryRepositoryPort.findByTitle(title).ifPresent(game -> {
                    throw new ApiException(ApiExceptionConstantsMessagesError.GameInventory.TITLE_ALREADY_PRESENT.formatted(title), HttpStatus.BAD_REQUEST);
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
            return Boolean.FALSE;
        }
        // Verificar si hay suficiente stock para cada título
        return titles.stream()
                .allMatch(title -> gameInventoryModels.stream()
                        .filter(model -> model.getTitle().equals(title))
                        .mapToLong(GameInventoryModel::getStock)
                        .sum() > 0);
    }

}
