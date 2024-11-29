package com.spotfinder.Models;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactUs, Long> {
    // You can define custom queries here if needed
}