package com.gastrotech.restoreserve.service;

import com.gastrotech.restoreserve.dto.TableRequestDTO;
import com.gastrotech.restoreserve.dto.TableResponseDTO;
import com.gastrotech.restoreserve.entity.RestaurantTable;
import com.gastrotech.restoreserve.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;

    public TableResponseDTO createTable(TableRequestDTO dto) {
        RestaurantTable table = new RestaurantTable();
        table.setName(dto.name());
        table.setCapacity(dto.capacity());
        return toDTO(tableRepository.save(table));
    }

    public List<TableResponseDTO> getAllTables() {
        return tableRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private TableResponseDTO toDTO(RestaurantTable table) {
        return new TableResponseDTO(table.getId(), table.getName(), table.getCapacity());
    }
}