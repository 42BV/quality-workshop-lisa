package nl._42.qualityws.cleancode.user;

import static org.junit.Assert.assertEquals;

import java.util.List;

import nl._42.qualityws.cleancode.shared.AbstractIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends AbstractIntegrationTest {

    private static final String USER_NAME   = "Henk";
    private static final String EMAIL       = "henk@mijnemail.nl";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void findByName() {
        createUser(USER_NAME, EMAIL);

        User foundUser = userService.findByName(USER_NAME);
        assertEquals(USER_NAME, foundUser.getName());
        assertEquals(EMAIL, foundUser.getEmail());
    }

    @Test
    public void findByEmail() {
        createUser(USER_NAME, EMAIL);

        List<User> foundUsers = userService.findByEmail(EMAIL);
        assertEquals(1, foundUsers.size());
        User foundUser = foundUsers.get(0);
        assertEquals(USER_NAME, foundUser.getName());
        assertEquals(EMAIL, foundUser.getEmail());
    }

    @Test
    public void findByEmailNull() {
        createUser(USER_NAME, EMAIL);

        List<User> foundUsers = userService.findByEmail(null);
        assertEquals(0, foundUsers.size());
    }

    private void createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);
    }

}
