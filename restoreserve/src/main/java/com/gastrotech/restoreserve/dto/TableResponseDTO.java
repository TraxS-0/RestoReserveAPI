package com.gastrotech.restoreserve.dto;

public record TableResponseDTO(
    Long id,
    String name,
    int capacity
) {}