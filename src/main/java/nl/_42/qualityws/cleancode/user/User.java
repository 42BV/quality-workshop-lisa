package nl._42.qualityws.cleancode.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import nl._42.qualityws.cleancode.shared.entity.AbstractEntity;

@Entity
@Table(name = "app_user")
public class User extends AbstractEntity {

    private String name;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
