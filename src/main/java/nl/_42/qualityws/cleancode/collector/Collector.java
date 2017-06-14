package nl._42.qualityws.cleancode.collector;

import javax.persistence.Entity;

import nl._42.qualityws.cleancode.shared.entity.AbstractEntity;

@Entity
public class Collector extends AbstractEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
