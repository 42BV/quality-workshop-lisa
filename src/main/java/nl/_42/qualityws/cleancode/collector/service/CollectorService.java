package nl._42.qualityws.cleancode.collector.service;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import nl._42.qualityws.cleancode.collector.Collector;

@Service
public class CollectorService {

    @Autowired
    private CollectorRepository collectorRepository;
    
    public Collector create(Collector collector) {
        notNull(collector, "Collector must not be null.");
        isTrue(collector.isNew(), "Collector to be created must not exist.");
        return collectorRepository.save(collector);
    }
    
    public Page<Collector> list(Pageable pageable) {
        return collectorRepository.findAll(pageable);
    }
}
