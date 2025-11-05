package org.example.user;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerIfAbsent(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username(username)
                            .build();
                    return userRepository.save(newUser);
                });
    }

    // Получаем username по telegramId (чтобы сравнивать)
//    @Cacheable(value = "UserNameById", key = "#telegramId")
//    public String getUsernameById(String telegramId) {
//        User user = userRepository.findById(telegramId)
//                .orElseThrow(()->  new IllegalArgumentException("User not found by telegramId: " + telegramId));
//        // Например:
//        return user.getUsername(); // временно
//    }

    @Cacheable(value = "allUsers", unless = "#result == null or #result.isEmpty()")
    public List<User> getAllUsersCached(){
        return userRepository.findAll();
    }
}
