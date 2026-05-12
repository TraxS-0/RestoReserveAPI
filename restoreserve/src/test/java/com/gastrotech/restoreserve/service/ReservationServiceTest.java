package com.gastrotech.restoreserve.service;

import com.gastrotech.restoreserve.dto.ReservationRequestDTO;
import com.gastrotech.restoreserve.exception.ResourceNotFoundException;
import com.gastrotech.restoreserve.repository.ReservationRepository;
import com.gastrotech.restoreserve.repository.TableRepository;
import com.gastrotech.restoreserve.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TableRepository tableRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void createReservation_shouldFail_whenDateIsInThePast() {
        ReservationRequestDTO dto = new ReservationRequestDTO(
                1L,
                LocalDateTime.now().minusDays(1),
                2
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.createReservation(dto, "testuser")
        );

        assertEquals("La reserva debe ser en una fecha futura", exception.getMessage());
        verifyNoInteractions(userRepository, tableRepository, reservationRepository);
    }

    @Test
    void createReservation_shouldFail_whenUserNotFound() {
        ReservationRequestDTO dto = new ReservationRequestDTO(
                1L,
                LocalDateTime.now().plusDays(1),
                2
        );

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.createReservation(dto, "testuser")
        );
    }
}