package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface listingRepository extends JpaRepository<listing, Long> {
}
