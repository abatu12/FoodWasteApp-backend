package com.example.foodwasteapp.controller;

import com.example.foodwasteapp.dto.ClaimDto;
import com.example.foodwasteapp.service.ClaimService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/claims")
public class ClaimController {

    private final ClaimService claimService;


    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }


    @GetMapping
    public List<ClaimDto> getAllClaims() {
        return claimService.getAll();
    }


    @GetMapping("/{id}")
    public ClaimDto getClaimById(@PathVariable Long id) {
        return claimService.getById(id);
    }


    @PostMapping
    public ClaimDto createClaim(@RequestBody ClaimDto dto) {
        return claimService.create(dto);
    }


    @PutMapping("/{id}")
    public ClaimDto updateClaim(
            @PathVariable Long id,
            @RequestBody ClaimDto dto
    ) {
        return claimService.update(id, dto);
    }


    @DeleteMapping("/{id}")
    public void deleteClaim(@PathVariable Long id) {
        claimService.delete(id);
    }
}
