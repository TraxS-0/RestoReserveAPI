package com.gastrotech.restoreserve.service;

import com.gastrotech.restoreserve.dto.ReservationRequestDTO;
import com.gastrotech.restoreserve.dto.ReservationResponseDTO;
import com.gastrotech.restoreserve.entity.Reservation;
import com.gastrotech.restoreserve.entity.ReservationStatus;
import com.gastrotech.restoreserve.entity.RestaurantTable;
import com.gastrotech.restoreserve.entity.User;
import com.gastrotech.restoreserve.exception.ResourceNotFoundException;
import com.gastrotech.restoreserve.repository.ReservationRepository;
import com.gastrotech.restoreserve.repository.TableRepository;
import com.gastrotech.restoreserve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;

    public ReservationResponseDTO createReservation(ReservationRequestDTO dto, String username) {
        if (dto.reservationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La reserva debe ser en una fecha futura");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        RestaurantTable table = tableRepository.findById(dto.tableId())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));

        LocalDateTime start = dto.reservationDate().minusHours(2);
        LocalDateTime end = dto.reservationDate().plusHours(2);

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                dto.tableId(), start, end, ReservationStatus.CANCELLED);

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("La mesa ya tiene una reserva en esa franja horaria");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setTable(table);
        reservation.setReservationDate(dto.reservationDate());
        reservation.setNumberOfGuests(dto.numberOfGuests());
        reservation.setStatus(ReservationStatus.PENDING);

        return toDTO(reservationRepository.save(reservation));
    }

    public List<ReservationResponseDTO> getReservations(String username, boolean isAdmin) {
        if (isAdmin) {
            return reservationRepository.findAll()
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }
        return reservationRepository.findByUserUsername(username)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    private ReservationResponseDTO toDTO(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getTable().getName(),
                reservation.getUser().getUsername(),
                reservation.getReservationDate(),
                reservation.getNumberOfGuests(),
                reservation.getStatus()
        );
    }
}