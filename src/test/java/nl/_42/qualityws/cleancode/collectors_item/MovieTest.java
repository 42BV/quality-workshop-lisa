package nl._42.qualityws.cleancode.collectors_item;

import static org.junit.Assert.assertEquals;

import nl._42.qualityws.cleancode.collector.Collector;

import org.junit.Test;

public class MovieTest {

    @Test
    public void setters_shouldSucceed_afterUsingConstructor() {
        final Collector collector = new Collector();
        collector.setName("Cornelis de Verzamelaar");
        final String expectedName = "The Wire";
        final String expectedImdbUrl = "http://www.imdb.com/title/tt0306414/";
        Movie movie = new Movie();
        movie.setName(expectedName);
        movie.setCollector(collector);
        movie.setImdbUrl(expectedImdbUrl);
        assertEquals(expectedName, movie.getName());
        assertEquals(expectedImdbUrl, movie.getImdbUrl());
        assertEquals(expectedImdbUrl, movie.getImdbUrl());
        assertEquals(collector.getName(), movie.getCollector().getName());
    }

}
