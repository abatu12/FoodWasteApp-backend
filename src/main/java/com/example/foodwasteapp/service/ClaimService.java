package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.claimDto;

import java.util.List;

public interface ClaimService {
    List<claimDto> getAll();
    claimDto getById(Long id);
    claimDto create(claimDto dto);
    claimDto update(Long id, claimDto dto);
    void delete(Long id);
}
