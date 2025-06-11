package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim,Long> {
}
