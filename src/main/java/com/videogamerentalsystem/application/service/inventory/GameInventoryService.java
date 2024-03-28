package com.videogamerentalsystem.application.service.inventory;

import com.videogamerentalsystem.common.UseCase;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.model.inventory.GameInventoryPriceModel;
import com.videogamerentalsystem.domain.port.in.inventory.GameInventoryUserCase;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryCommand;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryPriceCommand;
import com.videogamerentalsystem.domain.port.out.inventory.GameInventoryRepositoryPort;
import java.util.Optional;
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
}
