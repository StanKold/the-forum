package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Phone;
import com.example.forum.models.User;
import com.example.forum.repositories.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneServiceImpl implements PhoneService {

    public static final String ONLY_ADMINS_CAN_MODIFY_OR_DELETE_PHONE_NUMBERS = "Only not banned admins" +
            " can modify or delete phone numbers";
    private final PhoneRepository repository;

    @Autowired
    public PhoneServiceImpl(PhoneRepository repository) {
        this.repository = repository;
    }

    @Override
    public Phone addPhone(Phone phone, User admin) {
        checkModifyPermissions(admin);
        boolean duplicateExists = true;

        try {
            repository.getPhone(phone.getPhoneNumber());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Phone", "number", phone.getPhoneNumber());
        }
        phone.setUser(admin);
        return repository.addPhone(phone);
    }

    @Override
    public Phone updatePhone(Phone phone, User admin) {
        checkModifyPermissions(admin);
        boolean duplicateExists = true;

        try {
            Phone phoneToUpdate = repository.getPhone(phone.getPhoneNumber());
            if (phoneToUpdate.getPhoneId() == phone.getPhoneId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Phone", "number", phone.getPhoneNumber());
        }
        phone.setUser(admin);
        return repository.updatePhone(phone);
    }

    @Override
    public Phone getPhone(int phoneId) {
        return repository.getPhone(phoneId);
    }

    @Override
    public Phone getPhoneByUser(int userId) {
        return repository.getPhoneByUser(userId);
    }

    @Override
    public void deletePhone(Phone phoneToDelete, User admin) {
        checkModifyPermissions(admin);
        repository.deletePhone(phoneToDelete);
    }

    @Override
    public List<Phone> getPhones() {
        return repository.getAllPhones();
    }

    private void checkModifyPermissions(User user) {
        if (!(user.isAdmin() || user.isBlocked())) {
            throw new AuthorizationException
                    (ONLY_ADMINS_CAN_MODIFY_OR_DELETE_PHONE_NUMBERS);
        }
    }




}
