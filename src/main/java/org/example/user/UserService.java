package org.example.user;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerIfAbsent(Long telegramId, String username) {
        return userRepository.findById(telegramId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .telegramId(telegramId)
                            .username(username)
                            .build();
                    return userRepository.save(newUser);
                });
    }

    // Получаем username по telegramId (чтобы сравнивать)
    @Cacheable(value = "UserNameById", key = "#telegramId")
    public String getUsernameById(Long telegramId) {
        User user = userRepository.findById(telegramId)
                .orElseThrow(()->  new IllegalArgumentException("User not found by telegramId: " + telegramId));
        // Например:
        return user.getUsername(); // временно
    }

    @Cacheable(value = "allUsers", unless = "#result == null or #result.isEmpty()")
    public List<User> getAllUsersCached(){
        return userRepository.findAll();
    }
}
