package com.videogamerentalsystem.infraestucture.adapter.in.web.controller;

import com.videogamerentalsystem.domain.model.rental.RentalModel;
import com.videogamerentalsystem.domain.port.in.rental.RentalUserCase;
import com.videogamerentalsystem.domain.port.in.rental.command.RentalCommand;
import com.videogamerentalsystem.infraestucture.adapter.out.dto.rental.ResponseRentalDTO;
import com.videogamerentalsystem.infraestucture.adapter.out.mapper.rental.RentalResponseApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/video-game-rental-system/rental")
@RequiredArgsConstructor
public class RentalController {
    private final RentalUserCase rentalUserCase;
    private final RentalResponseApiMapper rentalResponseApiMapper;

    @PostMapping()
    public ResponseEntity<ResponseRentalDTO> create(@Valid @RequestBody RentalCommand rentalCommand) {
        RentalModel crateRental = this.rentalUserCase.create(rentalCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.rentalResponseApiMapper.toResponseApi(crateRental));
    }

    @GetMapping("/{rentalId}")
    public ResponseEntity<ResponseRentalDTO> get(@PathVariable(value = "rentalId") Long rentalId) {
        RentalModel rental = this.rentalUserCase.get(rentalId);
        return ResponseEntity.status(HttpStatus.OK).body(this.rentalResponseApiMapper.toResponseApi(rental));
    }

    @PutMapping("/{rentalId}/hand-back/game")
    public ResponseEntity<ResponseRentalDTO> handBackGame(@PathVariable(value = "rentalId") Long rentalId, @RequestParam(value = "product_id") Long productId) {
        RentalModel handBackGame = this.rentalUserCase.handBackGame(rentalId, productId);
        return ResponseEntity.status(HttpStatus.OK).body(this.rentalResponseApiMapper.toResponseApi(handBackGame));
    }

}
