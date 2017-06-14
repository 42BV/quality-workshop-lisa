package nl._42.qualityws.cleancode.collectors_item;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import nl._42.qualityws.cleancode.collector.Collector;
import nl._42.qualityws.cleancode.shared.entity.AbstractEntity;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CollectorsItem extends AbstractEntity {

    private String name;

    @ManyToOne
    private Collector collector;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

}
