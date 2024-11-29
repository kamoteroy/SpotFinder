package com.spotfinder.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactUsService {

    @Autowired
    private ContactRepository contactUsRepository;

    public void save(ContactUs contactUs) {
        contactUsRepository.save(contactUs);
    }
}
