package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.listing;
import com.example.foodwasteapp.dto.listingDto;
import com.example.foodwasteapp.repository.listingRepository;
import com.example.foodwasteapp.service.listingService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class listingServiceImpl implements listingService {

    private final listingRepository listingRepo;

    public listingServiceImpl(listingRepository listingRepo) {
        this.listingRepo = listingRepo;
    }

    @Override
    public List<listingDto> getAll() {
        return listingRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public listingDto getById(Long id) {
        listing l = listingRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Listing not found"));
        return toDto(l);
    }

    @Override
    public listingDto create(listingDto dto) {
        listing entity = toEntity(dto);
        listing saved = listingRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public listingDto update(Long id, listingDto dto) {
        listing existing = listingRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Listing not found"));
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setQuantity(dto.getQuantity());
        existing.setExpiryDate(dto.getExpiryDate());
        existing.setCreatedAt(dto.getCreatedAt());
        existing.setImage(dto.getImage());
        existing.setUserID(dto.getUserID());
        listing updated = listingRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        listingRepo.deleteById(id);
    }

    private listingDto toDto(listing l) {
        listingDto dto = new listingDto();
        dto.setId(l.getId());
        dto.setTitle(l.getTitle());
        dto.setDescription(l.getDescription());
        dto.setQuantity(l.getQuantity());
        dto.setExpiryDate(l.getExpiryDate());
        dto.setCreatedAt(l.getCreatedAt());
        dto.setImage(l.getImage());
        dto.setUserID(l.getUserID());
        return dto;
    }

    private listing toEntity(listingDto dto) {
        listing l = new listing();
        l.setTitle(dto.getTitle());
        l.setDescription(dto.getDescription());
        l.setQuantity(dto.getQuantity());
        l.setExpiryDate(dto.getExpiryDate());
        l.setCreatedAt(dto.getCreatedAt());
        l.setImage(dto.getImage());
        l.setUserID(dto.getUserID());
        return l;
    }
}
