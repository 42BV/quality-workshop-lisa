package nl._42.qualityws.cleancode._local.builder;

import java.io.IOException;

import javax.annotation.PostConstruct;

import nl._42.qualityws.cleancode.collectors_item.service.CollectorsItemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class TestDataLoader implements ResourceLoaderAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestDataLoader.class);

    private Resource amazonBooksCsv;
    private Resource imdbMoviesCsv;
    private Resource spotifyAlbumsCsv;

    @Autowired
    private CollectorsItemService collectorsItemService;

    @PostConstruct
    public void load() throws IOException {
        LOGGER.info("Loading testdata");
        collectorsItemService.importAlbums(spotifyAlbumsCsv.getInputStream());
        collectorsItemService.importBooks(amazonBooksCsv.getInputStream());
        collectorsItemService.importMovies(imdbMoviesCsv.getInputStream());
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        amazonBooksCsv = resourceLoader.getResource("classpath:/import/amazon_books.csv");
        imdbMoviesCsv = resourceLoader.getResource("classpath:/import/imdb_movies.csv");
        spotifyAlbumsCsv = resourceLoader.getResource("classpath:/import/spotify_albums.csv");
    }

}
