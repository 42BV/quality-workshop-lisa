package nl._42.qualityws.cleancode.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    @Query("select u from User u where upper(u.email) = upper(:email)")
    List<User> findUpperEmail(@Param("email") String email);

}
