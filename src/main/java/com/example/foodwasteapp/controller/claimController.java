package com.example.foodwasteapp.controller;

import com.example.foodwasteapp.service.ClaimService;
import com.example.foodwasteapp.dto.ClaimDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController               /
@RequestMapping("/claims")
public class ClaimController {

    @Autowired
    private ClaimService claimService;


    @GetMapping
    public List<ClaimDto> getAllClaims() {
        return claimService.findAll();
    }


    @GetMapping("/{id}")
    public ClaimDto getClaimById(@PathVariable Long id) {
        return claimService.findById(id);
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
        claimService.deleteById(id);
    }
}
