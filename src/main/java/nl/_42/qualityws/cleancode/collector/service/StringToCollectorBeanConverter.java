package nl._42.qualityws.cleancode.collector.service;

import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;

import io.beanmapper.BeanMapper;
import io.beanmapper.core.BeanFieldMatch;
import io.beanmapper.core.converter.BeanConverter;
import nl._42.qualityws.cleancode.collector.Collector;

class StringToCollectorBeanConverter implements BeanConverter {

    private final CollectorRepository repository;

    public StringToCollectorBeanConverter(ApplicationContext applicationContext) {
        this.repository = (CollectorRepository)(new Repositories(applicationContext)).getRepositoryFor(Collector.class);
    }

    @Override
    public Object convert(
            BeanMapper beanMapper, Object source,
            Class<?> targetClass, BeanFieldMatch beanFieldMatch) {

        if (source == null) {
            return null;
        }
        String name = (String)source;

        Collector collector = repository.findByName(name);
        if (collector == null) {
            collector = new Collector();
            collector.setName(name);
            repository.save(collector);
        }
        return collector;
    }

    @Override
    public boolean match(Class<?> sourceClass, Class<?> targetClass) {
        return  sourceClass.equals(String.class) &&
                targetClass.equals(Collector.class);
    }

}
