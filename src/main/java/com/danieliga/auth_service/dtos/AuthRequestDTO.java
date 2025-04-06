package com.danieliga.auth_service.dtos;

import lombok.Builder;
@Builder
public record AuthRequestDTO(String username, String password) {
}