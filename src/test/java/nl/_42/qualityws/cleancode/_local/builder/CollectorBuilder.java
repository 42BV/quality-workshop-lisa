package nl._42.qualityws.cleancode._local.builder;

import nl._42.beanie.BeanBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CollectorBuilder {

    @Autowired
    private BeanBuilder beanBuilder;
    
    public CollectorBuildCommand collector(String name) {
        return beanBuilder.startAs(CollectorBuildCommand.class).withName(name);
    }
}
