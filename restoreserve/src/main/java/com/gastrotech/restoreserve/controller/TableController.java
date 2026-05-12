package com.gastrotech.restoreserve.controller;

import com.gastrotech.restoreserve.dto.TableRequestDTO;
import com.gastrotech.restoreserve.dto.TableResponseDTO;
import com.gastrotech.restoreserve.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    public ResponseEntity<List<TableResponseDTO>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @PostMapping
    public ResponseEntity<TableResponseDTO> createTable(@Valid @RequestBody TableRequestDTO dto) {
        return new ResponseEntity<>(tableService.createTable(dto), HttpStatus.CREATED);
    }
}