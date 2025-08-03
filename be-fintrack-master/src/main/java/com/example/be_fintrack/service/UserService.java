package com.example.be_fintrack.service;

import com.example.be_fintrack.entity.User;
import com.example.be_fintrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public User create(User user) {
        return repo.save(user);
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public Optional<User> getById(Long id) {
        return repo.findById(id);
    }

    public User update(Long id, User updatedUser) {
        User user = repo.findById(id).orElseThrow();
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());
        user.setEmail(updatedUser.getEmail());
        user.setFullName(updatedUser.getFullName());
        return repo.save(user);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
