package com.gastrotech.restoreserve.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record ReservationRequestDTO(
    @NotNull(message = "La mesa es obligatoria")
    Long tableId,

    @NotNull(message = "La fecha y hora es obligatoria")
    @Future(message = "La reserva debe ser en una fecha futura")
    LocalDateTime reservationDate,

    @Min(value = 1, message = "Mínimo 1 comensal")
    @Max(value = 12, message = "Máximo 12 comensales por mesa")
    int numberOfGuests
) {}