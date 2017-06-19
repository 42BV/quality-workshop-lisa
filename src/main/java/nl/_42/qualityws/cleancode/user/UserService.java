package nl._42.qualityws.cleancode.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public List<User> findByEmail(String email) {
        return userRepository.findUpperEmail(email);
    }

}
