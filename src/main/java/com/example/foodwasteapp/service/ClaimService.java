package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.ClaimDto;

import java.util.List;

public interface ClaimService {
    List<ClaimDto> getAll();
    ClaimDto getById(Long id);
    ClaimDto create(ClaimDto dto);
    ClaimDto update(Long id, ClaimDto dto);
    void delete(Long id);
}
