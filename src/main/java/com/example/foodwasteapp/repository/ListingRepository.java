package com.example.foodwasteapp.repository;

import com.example.foodwasteapp.dbmodel.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
}
