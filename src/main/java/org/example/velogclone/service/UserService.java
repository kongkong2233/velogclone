package org.example.velogclone.service;

import lombok.RequiredArgsConstructor;
import org.example.velogclone.domain.User;
import org.example.velogclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String username, String email, String password, String usernick) {
        User user = new User();
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserNick(usernick);
        user.setAdmin(false);
        return userRepository.save(user);
    }

    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    //사용자 있나없나
    public boolean validateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUserName(username);
        return userOptional.isPresent() && userOptional.get().getPassword().equals(password);
    }
}
