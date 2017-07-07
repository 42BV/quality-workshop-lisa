package nl._42.qualityws.cleancode.collector.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import nl._42.qualityws.cleancode.collector.Collector;
import nl._42.qualityws.cleancode.shared.AbstractIntegrationTest;
import nl._42.qualityws.cleancode._local.builder.CollectorBuilder;

public class CollectorRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CollectorRepository ownerRepository;
    @Autowired
    private CollectorBuilder collectorBuilder;

    @Test
    public void findOne_shouldSucceed_afterEntitySaved() {
        String expectedName = "Cornelis de Verzamelaar";
        Collector owner = collectorBuilder.collector(expectedName).save();
        owner = ownerRepository.findOne(owner.getId());
        assertEquals(expectedName, owner.getName());
    }

}