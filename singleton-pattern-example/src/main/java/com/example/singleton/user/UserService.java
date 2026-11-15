package com.example.singleton.user;

/** Memora의 UserService를 단순화한 것. 필드는 주입받은 저장소 하나뿐이다. */
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String greet(int userId) {
        return userRepository.findName(userId) + " 님, 안녕하세요";
    }
}
