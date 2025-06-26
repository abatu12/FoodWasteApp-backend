package com.example.foodwasteapp.jUnitTests;

import com.example.foodwasteapp.dbmodel.Listing;
import com.example.foodwasteapp.dto.ListingDto;
import com.example.foodwasteapp.repository.ListingRepository;
import com.example.foodwasteapp.service.impl.ListingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

class ListingServiceImplTest {

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private ListingServiceImpl listingService; // pretpostavka da postoji

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        Listing listing = new Listing();
        listing.setId(1L);
        listing.setTitle("Test title");
        listing.setDescription("Test desc");
        listing.setQuantity(10);
        listing.setExpiryDate(LocalDate.now().plusDays(10));
        listing.setCreatedAt(LocalDateTime.now());
        listing.setImage("image.png");
        listing.setUserID(2L);

        when(listingRepository.findAll()).thenReturn(List.of(listing));

        List<ListingDto> result = listingService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test title", result.get(0).getTitle());

        verify(listingRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        Listing listing = new Listing();
        listing.setId(1L);
        listing.setTitle("Test title");

        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));

        ListingDto dto = listingService.getById(1L);

        assertNotNull(dto);
        assertEquals("Test title", dto.getTitle());
    }



    @Test
    void testCreate() {
        ListingDto dto = new ListingDto();
        dto.setTitle("New listing");
        dto.setQuantity(5);

        Listing savedListing = new Listing();
        savedListing.setId(1L);
        savedListing.setTitle("New listing");
        savedListing.setQuantity(5);

        when(listingRepository.save(any(Listing.class))).thenReturn(savedListing);

        ListingDto result = listingService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New listing", result.getTitle());
    }

    @Test
    void testUpdate() {
        ListingDto dto = new ListingDto();
        dto.setTitle("Updated listing");
        dto.setQuantity(8);

        Listing existingListing = new Listing();
        existingListing.setId(1L);
        existingListing.setTitle("Old listing");

        when(listingRepository.findById(1L)).thenReturn(Optional.of(existingListing));
        when(listingRepository.save(any(Listing.class))).thenAnswer(i -> i.getArgument(0));

        ListingDto result = listingService.update(1L, dto);

        assertNotNull(result);
        assertEquals("Updated listing", result.getTitle());
        assertEquals(8, result.getQuantity());
    }

    @Test
    void testDelete() {
        doNothing().when(listingRepository).deleteById(1L);
        listingService.delete(1L);
        verify(listingRepository, times(1)).deleteById(1L);
    }
}
