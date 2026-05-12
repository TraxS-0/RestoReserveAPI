package com.gastrotech.restoreserve.dto;

public record AuthResponseDTO(
    String token,
    String username,
    String role
) {}