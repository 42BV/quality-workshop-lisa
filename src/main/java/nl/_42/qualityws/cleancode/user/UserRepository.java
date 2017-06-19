package nl._42.qualityws.cleancode.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    List<User> findByEmailIgnoreCase(String email);

}
