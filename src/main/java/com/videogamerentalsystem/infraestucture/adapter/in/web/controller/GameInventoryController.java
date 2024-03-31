package com.videogamerentalsystem.infraestucture.adapter.in.web.controller;


import com.videogamerentalsystem.domain.model.inventory.GameInventoryModel;
import com.videogamerentalsystem.domain.port.in.inventory.GameInventoryUserCase;
import com.videogamerentalsystem.domain.port.in.inventory.commad.GameInventoryCommand;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.inventory.ResponseInventoryDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.mapper.inventory.GameInventoryResponseApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/video-game-rental-system/inventory")
@RequiredArgsConstructor
public class GameInventoryController {

    private final GameInventoryUserCase gameInventoryUserCase;
    private final GameInventoryResponseApiMapper gameInventoryResponseApiMapper;

    @PostMapping()
    public ResponseEntity<ResponseInventoryDTO> create(@Valid @RequestBody GameInventoryCommand gameInventoryCommand) {
        GameInventoryModel createGame = this.gameInventoryUserCase.create(gameInventoryCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.gameInventoryResponseApiMapper.toResponseApi(createGame));
    }
    @GetMapping("/{inventoryId}")
    public ResponseEntity<ResponseInventoryDTO> findInventoryById (@PathVariable Long inventoryId) {
      return  this.gameInventoryUserCase
              .findInventoryById(inventoryId)
              .map(this.gameInventoryResponseApiMapper::toResponseApi)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.noContent().build());
    }

}
