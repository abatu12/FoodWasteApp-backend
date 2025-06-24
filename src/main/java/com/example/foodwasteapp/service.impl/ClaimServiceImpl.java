package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.Claim;
import com.example.foodwasteapp.dto.ClaimDto;
import com.example.foodwasteapp.repository.ClaimRepository;
import com.example.foodwasteapp.service.ClaimService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepo;

    public ClaimServiceImpl(ClaimRepository claimRepo) {
        this.claimRepo = claimRepo;
    }

    @Override
    public List<ClaimDto> getAll() {
        return claimRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ClaimDto getById(Long id) {
        Claim c = claimRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Claim not found"));
        return toDto(c);
    }

    @Override
    public ClaimDto create(ClaimDto dto) {
        Claim entity = toEntity(dto);
        Claim saved = claimRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public ClaimDto update(Long id, ClaimDto dto) {
        Claim existing = claimRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Claim not found"));
        existing.setRequesterID(dto.getRequesterID());
        existing.setListingID(dto.getListingID());
        existing.setStatus(dto.getStatus());
        existing.setRequestedAt(dto.getRequestedAt());
        Claim updated = claimRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        claimRepo.deleteById(id);
    }

    private ClaimDto toDto(Claim c) {
        ClaimDto dto = new ClaimDto();
        dto.setId(c.getId());
        dto.setRequesterID(c.getRequesterID());
        dto.setListingID(c.getListingID());
        dto.setStatus(c.getStatus());
        dto.setRequestedAt(c.getRequestedAt());
        return dto;
    }

    private Claim toEntity(ClaimDto dto) {
        Claim c = new Claim();
        c.setRequesterID(dto.getRequesterID());
        c.setListingID(dto.getListingID());
        c.setStatus(dto.getStatus());
        c.setRequestedAt(dto.getRequestedAt());
        return c;
    }
}
