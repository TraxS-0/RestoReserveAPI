package com.gastrotech.restoreserve.repository;

import com.gastrotech.restoreserve.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
}