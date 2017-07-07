package nl._42.qualityws.cleancode.collectors_item.service;

import static org.junit.Assert.assertEquals;

import nl._42.qualityws.cleancode.collector.Collector;
import nl._42.qualityws.cleancode.collectors_item.Movie;
import nl._42.qualityws.cleancode.shared.AbstractIntegrationTest;
import nl._42.qualityws.cleancode._local.builder.CollectorBuilder;
import nl._42.qualityws.cleancode._local.builder.CollectorsItemBuilder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MovieRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CollectorBuilder collectorBuilder;
    @Autowired
    private CollectorsItemBuilder collectorsItemBuilder;

    @Test
    public void findOne_shouldSucceed_afterEntitySaved() {
        final Collector collector = collectorBuilder.collector("Cornelis de Verzamelaar").save();
        final String expectedName = "The Wire";
        final String expectedImdbUrl = "http://www.imdb.com/title/tt0306414/";
        
        Movie movie = collectorsItemBuilder.movie(expectedName, collector)
                .withImdbUrl(expectedImdbUrl).save();
        
        movie = movieRepository.findOne(movie.getId());
        assertEquals(expectedName, movie.getName());
        assertEquals(expectedImdbUrl, movie.getImdbUrl());
        assertEquals(expectedImdbUrl, movie.getImdbUrl());
        assertEquals(collector.getName(), movie.getCollector().getName());
    }

}
