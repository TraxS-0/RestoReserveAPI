package com.gastrotech.restoreserve.dto;

import com.gastrotech.restoreserve.entity.ReservationStatus;
import java.time.LocalDateTime;

public record ReservationResponseDTO(
    Long id,
    String tableName,
    String customerName,
    LocalDateTime reservationDate,
    int numberOfGuests,
    ReservationStatus status
) {}