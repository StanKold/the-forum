package com.example.forum.repositories;

import com.example.forum.models.Phone;

import java.util.List;

public interface PhoneRepository {
    Phone addPhone(Phone phone);

    Phone updatePhone(Phone phone);

    Phone getPhone(int phoneId);

    Phone getPhone(String phoneNumber);

    Phone getPhoneByUser(int userId);

    void deletePhone(Phone phoneToDelete);

    List<Phone> getAllPhones();

}
