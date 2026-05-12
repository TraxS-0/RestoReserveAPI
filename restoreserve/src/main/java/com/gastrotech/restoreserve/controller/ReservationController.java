package com.gastrotech.restoreserve.controller;

import com.gastrotech.restoreserve.dto.ReservationRequestDTO;
import com.gastrotech.restoreserve.dto.ReservationResponseDTO;
import com.gastrotech.restoreserve.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getReservations(Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return ResponseEntity.ok(reservationService.getReservations(username, isAdmin));
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(
            @Valid @RequestBody ReservationRequestDTO dto,
            Authentication authentication) {
        String username = authentication.getName();
        return new ResponseEntity<>(
                reservationService.createReservation(dto, username),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}