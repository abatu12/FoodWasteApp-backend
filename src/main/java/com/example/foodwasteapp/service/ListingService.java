package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.listingDto;
import java.util.List;

public interface ListingService {
    List<listingDto> getAll();
    listingDto getById(Long id);
    listingDto create(listingDto dto);
    listingDto update(Long id, listingDto dto);
    void delete(Long id);
}
