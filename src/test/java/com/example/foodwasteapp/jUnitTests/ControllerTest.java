package com.example.foodwasteapp.jUnitTests;

import com.example.foodwasteapp.controller.ClaimController;
import com.example.foodwasteapp.dto.ClaimDto;
import com.example.foodwasteapp.service.ClaimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClaimControllerUnitTest {

    @Mock
    private ClaimService claimService;

    @InjectMocks
    private ClaimController claimController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllClaims_shouldReturnListOfClaims() {
        ClaimDto c1 = new ClaimDto();
        c1.setId(1L);
        c1.setRequesterID(100L);
        c1.setListingID(200L);
        c1.setStatus("OPEN");
        c1.setRequestedAt(LocalDateTime.now());

        ClaimDto c2 = new ClaimDto();
        c2.setId(2L);
        c2.setRequesterID(101L);
        c2.setListingID(201L);
        c2.setStatus("CLOSED");
        c2.setRequestedAt(LocalDateTime.now());

        List<ClaimDto> claims = Arrays.asList(c1, c2);

        when(claimService.getAll()).thenReturn(claims);

        List<ClaimDto> result = claimController.getAllClaims();

        assertEquals(2, result.size());
        assertEquals("OPEN", result.get(0).getStatus());
        assertEquals(101L, result.get(1).getRequesterID());

        verify(claimService).getAll();
    }

    @Test
    void getClaimById_shouldReturnClaim() {
        ClaimDto claim = new ClaimDto();
        claim.setId(1L);
        claim.setRequesterID(100L);
        claim.setListingID(200L);
        claim.setStatus("OPEN");
        claim.setRequestedAt(LocalDateTime.now());

        when(claimService.getById(1L)).thenReturn(claim);

        ClaimDto result = claimController.getClaimById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("OPEN", result.getStatus());

        verify(claimService).getById(1L);
    }

    @Test
    void createClaim_shouldReturnCreatedClaim() {
        ClaimDto input = new ClaimDto();
        input.setRequesterID(100L);
        input.setListingID(200L);
        input.setStatus("NEW");
        input.setRequestedAt(LocalDateTime.now());

        ClaimDto created = new ClaimDto();
        created.setId(1L);
        created.setRequesterID(100L);
        created.setListingID(200L);
        created.setStatus("NEW");
        created.setRequestedAt(input.getRequestedAt());

        when(claimService.create(input)).thenReturn(created);

        ClaimDto result = claimController.createClaim(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("NEW", result.getStatus());

        verify(claimService).create(input);
    }

    @Test
    void updateClaim_shouldReturnUpdatedClaim() {
        ClaimDto input = new ClaimDto();
        input.setRequesterID(100L);
        input.setListingID(200L);
        input.setStatus("UPDATED");
        input.setRequestedAt(LocalDateTime.now());

        ClaimDto updated = new ClaimDto();
        updated.setId(1L);
        updated.setRequesterID(100L);
        updated.setListingID(200L);
        updated.setStatus("UPDATED");
        updated.setRequestedAt(input.getRequestedAt());

        when(claimService.update(1L, input)).thenReturn(updated);

        ClaimDto result = claimController.updateClaim(1L, input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("UPDATED", result.getStatus());

        verify(claimService).update(1L, input);
    }

    @Test
    void deleteClaim_shouldCallServiceDelete() {
        doNothing().when(claimService).delete(1L);

        claimController.deleteClaim(1L);

        verify(claimService).delete(1L);
    }
}
