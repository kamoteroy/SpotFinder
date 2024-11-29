package com.spotfinder.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactUsService {

    private final ContactRepository contactUsRepository;

    @Autowired
    public ContactUsService(ContactRepository contactUsRepository) {
        this.contactUsRepository = contactUsRepository;
    }

    public void save(ContactUs contact) {
        // Save the contact details to the database
        contactUsRepository.save(contact);
    }
}