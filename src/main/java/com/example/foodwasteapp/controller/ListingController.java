package com.example.foodwasteapp.controller;

import com.example.foodwasteapp.dto.ListingDto;
import com.example.foodwasteapp.service.ListingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping
    public List<ListingDto> getAllListings() {
        return listingService.getAll();
    }

    @GetMapping("/{id}")
    public ListingDto getListingById(@PathVariable Long id) {
        return listingService.getById(id);
    }

    @PostMapping
    public ListingDto createListing(@RequestBody ListingDto dto) {
        return listingService.create(dto);
    }

    @PutMapping("/{id}")
    public ListingDto updateListing(
            @PathVariable Long id,
            @RequestBody ListingDto dto
    ) {
        return listingService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable Long id) {
        listingService.delete(id);
    }
}
