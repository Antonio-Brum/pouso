package com.pouso.service;

import com.pouso.model.Person;
import com.pouso.repository.PersonRepository;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PersonRepository personRepository;

    // private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(PersonRepository personRepository) {
        this.personRepository = personRepository;
        // this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Person login(String email, String password) {
        if (email == null || email.isBlank()) {
            return null;
        }

        if (password == null || password.isBlank()) {
            return null;
        }

        if (!validateEmail(email)) {
            return null;
        }
        Person person = personRepository.buscarPorEmail(email);

        if (person == null) {
            return null;
        }

        // if (!passwordEncoder.matches(password, person.getPassword())) {
        //     return null;
        // }
        if (!person.getPassword().equals(password)) {
            return null;
        }
        return person;
    }

    private boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }
}
