package com.example.foodwasteapp.service;
import com.example.foodwasteapp.dto.ListingDto;
import java.util.List;

public interface ListingService {
    List<ListingDto> getAll();
    ListingDto getById(Long id);
    ListingDto create(ListingDto dto);
    ListingDto update(Long id, ListingDto dto);
    void delete(Long id);
}
