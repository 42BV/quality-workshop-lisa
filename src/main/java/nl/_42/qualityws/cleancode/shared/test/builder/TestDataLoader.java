package nl._42.qualityws.cleancode.shared.test.builder;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import nl._42.qualityws.cleancode.collectors_item.service.CollectorsItemService;

@Component
@Profile("!test")
public class TestDataLoader implements ResourceLoaderAware {

    private Resource amazonBooksCsv;
    private Resource imdbMoviesCsv;
    private Resource spotifyAlbumsCsv;

    @Autowired
    private CollectorsItemService collectorsItemService;

    @PostConstruct
    public void load() throws IOException {
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
