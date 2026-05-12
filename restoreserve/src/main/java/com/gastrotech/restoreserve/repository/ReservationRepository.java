package com.gastrotech.restoreserve.repository;

import com.gastrotech.restoreserve.entity.Reservation;
import com.gastrotech.restoreserve.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserUsername(String username);

    @Query("SELECT r FROM Reservation r WHERE r.table.id = :tableId " +
           "AND r.status != :cancelled " +
           "AND r.reservationDate BETWEEN :start AND :end")
    List<Reservation> findConflictingReservations(
        @Param("tableId") Long tableId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("cancelled") ReservationStatus cancelled
    );
}