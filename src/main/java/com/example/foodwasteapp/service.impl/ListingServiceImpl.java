package com.example.foodwasteapp.service.impl;

import com.example.foodwasteapp.dbmodel.Listing;
import com.example.foodwasteapp.dto.ListingDto;
import com.example.foodwasteapp.repository.ListingRepository;
import com.example.foodwasteapp.service.ListingService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepo;

    public ListingServiceImpl(ListingRepository listingRepo) {
        this.listingRepo = listingRepo;
    }

    @Override
    public List<ListingDto> getAll() {
        return listingRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ListingDto getById(Long id) {
        Listing l = listingRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Listing not found"));
        return toDto(l);
    }

    @Override
    public ListingDto create(ListingDto dto) {
        Listing entity = toEntity(dto);
        Listing saved = listingRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public ListingDto update(Long id, ListingDto dto) {
        Listing existing = listingRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Listing not found"));
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setQuantity(dto.getQuantity());
        existing.setExpiryDate(dto.getExpiryDate());
        existing.setCreatedAt(dto.getCreatedAt());
        existing.setImage(dto.getImage());
        existing.setUserID(dto.getUserID());
        Listing updated = listingRepo.save(existing);
        return toDto(updated);
    }

    @Override
    public void delete(Long id) {
        listingRepo.deleteById(id);
    }

    private ListingDto toDto(Listing l) {
        ListingDto dto = new ListingDto();
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

    private Listing toEntity(ListingDto dto) {
        Listing l = new Listing();
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
