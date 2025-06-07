package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.claim;
import com.example.foodwasteapp.dto.claimDto;
import com.example.foodwasteapp.repository.claimRepository;
import com.example.foodwasteapp.service.claimService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class claimServiceImpl implements claimService {

    private final claimRepository claimRepo;

    public claimServiceImpl(claimRepository claimRepo) {
        this.claimRepo = claimRepo;
    }

    @Override
    public List<claimDto> getAll() {
        return claimRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public claimDto getById(Long id) {
        claim c = claimRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Claim not found"));
        return toDto(c);
    }

    @Override
    public claimDto create(claimDto dto) {
        claim entity = toEntity(dto);
        claim saved = claimRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public claimDto update(Long id, claimDto dto) {
        claim existing = claimRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Claim not found"));
        existing.setRequesterID(dto.getRequesterID());
        existing.setListingID(dto.getListingID());
        existing.setStatus(dto.getStatus());
        existing.setRequestedAt(dto.getRequestedAt());
        claim updated = claimRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        claimRepo.deleteById(id);
    }

    private claimDto toDto(claim c) {
        claimDto dto = new claimDto();
        dto.setId(c.getId());
        dto.setRequesterID(c.getRequesterID());
        dto.setListingID(c.getListingID());
        dto.setStatus(c.getStatus());
        dto.setRequestedAt(c.getRequestedAt());
        return dto;
    }

    private claim toEntity(claimDto dto) {
        claim c = new claim();
        c.setRequesterID(dto.getRequesterID());
        c.setListingID(dto.getListingID());
        c.setStatus(dto.getStatus());
        c.setRequestedAt(dto.getRequestedAt());
        return c;
    }
}
