package com.example.forum.services;

import com.example.forum.models.Phone;
import com.example.forum.models.User;

import java.util.List;

public interface PhoneService {
    Phone addPhone(Phone phone, User admin);

    Phone updatePhone(Phone phone, User admin);

    Phone getPhone(int phoneId);

    Phone getPhoneByUser(int userId);

    void deletePhone(Phone phone, User admin);

    List<Phone> getPhones();
}
