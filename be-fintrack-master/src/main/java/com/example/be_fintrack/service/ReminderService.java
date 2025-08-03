package com.example.be_fintrack.service;

import com.example.be_fintrack.entity.Reminder;
import com.example.be_fintrack.entity.User;
import com.example.be_fintrack.repository.ReminderRepository;
import com.example.be_fintrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private UserRepository userRepository;

    public Reminder create(Reminder r) {
        return reminderRepository.save(r);
    }

    public List<Reminder> getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> reminderRepository.findByUserId(user.getId()))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));
    }

    public Reminder update(Long id, Reminder updated) {
        Reminder r = reminderRepository.findById(id).orElseThrow();
        r.setTitle(updated.getTitle());
        r.setContent(updated.getContent());
        r.setRemindDate(updated.getRemindDate());
        r.setDone(updated.isDone());
        return reminderRepository.save(r);
    }

    public void delete(Long id) {
        reminderRepository.deleteById(id);
    }
}
