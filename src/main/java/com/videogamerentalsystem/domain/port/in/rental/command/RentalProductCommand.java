package com.videogamerentalsystem.domain.port.in.rental.command;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RentalProductCommand(@NotBlank String title,
                                    @NotNull @Future LocalDateTime endDate) {}