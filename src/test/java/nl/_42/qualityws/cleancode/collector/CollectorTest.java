package nl._42.qualityws.cleancode.collector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollectorTest {

    @Test
    public void setName_shouldSucceed_afterUsingConstructor() {
        final String expectedName = "Cornelis de Verzamelaar";
        Collector owner = new Collector();
        owner.setName(expectedName);
        assertEquals(expectedName, owner.getName());
    }

}