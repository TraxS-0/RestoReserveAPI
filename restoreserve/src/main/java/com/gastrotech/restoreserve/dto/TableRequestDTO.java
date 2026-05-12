package com.gastrotech.restoreserve.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record TableRequestDTO(
    @NotBlank(message = "El nombre es obligatorio")
    String name,

    @Min(value = 1, message = "La capacidad mínima es 1")
    int capacity
) {}