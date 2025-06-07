package com.example.foodwasteapp.controller;

import com.example.foodwasteapp.dto.claimDto;
import com.example.foodwasteapp.service.ClaimService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/claims")
public class claimController {

    private final ClaimService claimService;


    public claimController(ClaimService claimService) {
        this.claimService = claimService;
    }


    @GetMapping
    public List<claimDto> getAllClaims() {
        return claimService.getAll();
    }


    @GetMapping("/{id}")
    public claimDto getClaimById(@PathVariable Long id) {
        return claimService.getById(id);
    }


    @PostMapping
    public claimDto createClaim(@RequestBody claimDto dto) {
        return claimService.create(dto);
    }


    @PutMapping("/{id}")
    public claimDto updateClaim(
            @PathVariable Long id,
            @RequestBody claimDto dto
    ) {
        return claimService.update(id, dto);
    }


    @DeleteMapping("/{id}")
    public void deleteClaim(@PathVariable Long id) {
        claimService.delete(id);
    }
}
