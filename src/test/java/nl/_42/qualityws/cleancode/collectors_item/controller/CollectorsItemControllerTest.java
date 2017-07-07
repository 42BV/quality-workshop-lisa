package nl._42.qualityws.cleancode.collectors_item.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl._42.qualityws.cleancode.collector.Collector;
import nl._42.qualityws.cleancode.collectors_item.Album;
import nl._42.qualityws.cleancode.collectors_item.Book;
import nl._42.qualityws.cleancode.collectors_item.Movie;
import nl._42.qualityws.cleancode.collectors_item.service.CollectorsItemService;
import nl._42.qualityws.cleancode.shared.AbstractWebIntegrationTest;
import nl._42.qualityws.cleancode._local.builder.CollectorBuilder;
import nl._42.qualityws.cleancode._local.builder.CollectorsItemBuilder;

public class CollectorsItemControllerTest extends AbstractWebIntegrationTest {

    @Autowired
    private CollectorsItemBuilder itemBuilder;
    @Autowired
    private CollectorBuilder collectorBuilder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CollectorsItemService service;

    @Test
    public void listMovies_shouldSucceed_whenFirstPageIsRequested() throws Exception {
        Collector collector = collectorBuilder.collector("Jan de Vries").save();
        itemBuilder.movie("The Wire", collector).withImdbUrl("http://www.imdb.com/title/tt0306414/").save();
        itemBuilder.movie("John Wick: chapter 2", collector).withImdbUrl("http://www.imdb.com/title/tt4425200/").save();

        webClient.perform(get("/items/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].collector.name").value("Jan de Vries"))
                .andExpect(jsonPath("$.content[0].name").value("John Wick: chapter 2"))
                .andExpect(jsonPath("$.content[0].imdbUrl").value("http://www.imdb.com/title/tt4425200/"));
    }

    @Test
    public void listAlbums_shouldSucceed_whenFirstPageIsRequested() throws Exception {
        Collector collector = collectorBuilder.collector("Yvonne IJzer").save();
        itemBuilder
                .album("Repentless", collector)
                .withSpotifyUrl("https://play.spotify.com/album/4rZuYdMyEhEtJh7awIO9sg")
                .withArtist("Slayer")
                .save();
        itemBuilder
                .album("Aventine", collector)
                .withSpotifyUrl("https://play.spotify.com/album/5mdWrhN59Jte2qZeLVKBJC")
                .withArtist("Agnes Obel")
                .save();

        webClient.perform(get("/items/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].collector.name").value("Yvonne IJzer"))
                .andExpect(jsonPath("$.content[0].name").value("Aventine"))
                .andExpect(jsonPath("$.content[0].artist").value("Agnes Obel"))
                .andExpect(jsonPath("$.content[0].spotifyUrl").value("https://play.spotify.com/album/5mdWrhN59Jte2qZeLVKBJC"));
    }

    @Test
    public void listBooks_shouldSucceed_whenFirstPageIsRequested() throws Exception {
        Collector collector = collectorBuilder.collector("Ahmar Warraq").save();
        itemBuilder
                .book("John Boyd", collector)
                .withAmazonUrl("https://www.amazon.com/dp/0316796883")
                .withAuthor("Robert Coram")
                .save();
        itemBuilder
                .book("Dr. Deming", collector)
                .withAmazonUrl("https://www.amazon.com/dp/0671746219")
                .withAuthor("John Deming")
                .save();


        webClient.perform(get("/items/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].collector.name").value("Ahmar Warraq"))
                .andExpect(jsonPath("$.content[0].name").value("Dr. Deming"))
                .andExpect(jsonPath("$.content[0].author").value("John Deming"))
                .andExpect(jsonPath("$.content[0].amazonUrl").value("https://www.amazon.com/dp/0671746219"));
    }

    @Test
    public void createMovie_shouldSucceed_whenValidFormIsPosted() throws Exception {
        Collector collector = collectorBuilder.collector("Jan de Vries").save();
        MovieForm form = new MovieForm();
        form.collector = collector.getId();
        form.name = "The Wire";
        form.imdbUrl = "http://www.imdb.com/title/tt4425200/";

        webClient.perform(post("/items/movies")
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collector.name").value("Jan de Vries"))
                .andExpect(jsonPath("$.name").value("The Wire"))
                .andExpect(jsonPath("$.imdbUrl").value("http://www.imdb.com/title/tt4425200/"));
    }
    
    @Test
    public void createMovie_shouldFail_whenInvalidFormIsPosted() throws Exception {
        webClient.perform(post("/items/movies")
                .content(objectMapper.writeValueAsString(new MovieForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
                .andExpect(jsonPath("$.fieldErrors[0].code").value("NotEmpty"));
    }
    
    @Test
    public void createAlbum_shouldSucceed_whenValidFormIsPosted() throws Exception {
        Collector collector = collectorBuilder.collector("Yvonne IJzer").save();
        AlbumForm form = new AlbumForm();
        form.collector = collector.getId();
        form.name = "Aventine";
        form.artist = "Agnes Obel";
        form.spotifyUrl = "https://play.spotify.com/album/5mdWrhN59Jte2qZeLVKBJC";

        webClient.perform(post("/items/albums")
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collector.name").value("Yvonne IJzer"))
                .andExpect(jsonPath("$.name").value("Aventine"))
                .andExpect(jsonPath("$.artist").value("Agnes Obel"))
                .andExpect(jsonPath("$.spotifyUrl").value("https://play.spotify.com/album/5mdWrhN59Jte2qZeLVKBJC"));
    }
    
    @Test
    public void createAlbum_shouldFail_whenInvalidFormIsPosted() throws Exception {
        webClient.perform(post("/items/albums")
                .content(objectMapper.writeValueAsString(new AlbumForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
                .andExpect(jsonPath("$.fieldErrors[0].code").value("NotEmpty"));
    }
    
    @Test
    public void createBook_shouldSucceed_whenValidFormIsPosted() throws Exception {
        Collector collector = collectorBuilder.collector("Ahmar Warraq").save();
        BookForm form = new BookForm();
        form.collector = collector.getId();
        form.name = "John Boyd";
        form.author = "Robert Coram";
        form.amazonUrl = "https://www.amazon.com/dp/0316796883";

        webClient.perform(post("/items/books")
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collector.name").value("Ahmar Warraq"))
                .andExpect(jsonPath("$.name").value("John Boyd"))
                .andExpect(jsonPath("$.author").value("Robert Coram"))
                .andExpect(jsonPath("$.amazonUrl").value("https://www.amazon.com/dp/0316796883"));
    }
    
    @Test
    public void createBook_shouldFail_whenInvalidFormIsPosted() throws Exception {
        webClient.perform(post("/items/books")
                .content(objectMapper.writeValueAsString(new BookForm())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
                .andExpect(jsonPath("$.fieldErrors[0].code").value("NotEmpty"));
    }
    
    @Test
    public void uploadBooks_shouldSucceed_whenBooksCsvIsProvided() throws Exception {
        InputStream booksCsv = webApplicationContext.getResource("classpath:/import/amazon_books.csv").getInputStream();
        
        webClient.perform(fileUpload("/items/books/upload")
                .file(new MockMultipartFile("csv", booksCsv)))
                .andExpect(status().isOk());
        
        Page<Book> books = service.listBooks(new PageRequest(0, 10));
        assertEquals(6, books.getTotalElements());
    }
    
    @Test
    public void uploadBooks_shouldFail_whenInvalidCsvIsProvided() throws Exception {
        InputStream invalidCsv = webApplicationContext.getResource("classpath:/import/imdb_movies.csv").getInputStream();
        
        webClient.perform(fileUpload("/items/books/upload")
                .file(new MockMultipartFile("csv", invalidCsv)))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void uploadMovies_shouldSucceed_whenMoviesCsvIsProvided() throws Exception {
        InputStream moviesCsv = webApplicationContext.getResource("classpath:/import/imdb_movies.csv").getInputStream();
        
        webClient.perform(fileUpload("/items/movies/upload")
                .file(new MockMultipartFile("csv", moviesCsv)))
                .andExpect(status().isOk());
        
        Page<Movie> movies = service.listMovies(new PageRequest(0, 10));
        assertEquals(11, movies.getTotalElements());
        Movie movie = movies.getContent().get(0);
        assertEquals("The Shawshank Redemption", movie.getName());
        assertEquals("9.3", movie.getImdbRating());
    }
    
    @Test
    public void uploadMovies_shouldFail_whenInvalidCsvIsProvided() throws Exception {
        InputStream invalidCsv = webApplicationContext.getResource("classpath:/import/amazon_books.csv").getInputStream();
        
        webClient.perform(fileUpload("/items/movies/upload")
                .file(new MockMultipartFile("csv", invalidCsv)))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void uploadAlbums_shouldSucceed_whenAlbumsCsvIsProvided() throws Exception {
        InputStream albumsCsv = webApplicationContext.getResource("classpath:/import/spotify_albums.csv").getInputStream();
        
        webClient.perform(fileUpload("/items/albums/upload")
                .file(new MockMultipartFile("csv", albumsCsv)))
                .andExpect(status().isOk());
        
        Page<Album> albums = service.listAlbums(new PageRequest(0, 10));
        assertEquals(10, albums.getTotalElements());
    }
    
    @Test
    public void uploadAlbums_shouldFail_whenInvalidCsvIsProvided() throws Exception {
        InputStream invalidCsv = webApplicationContext.getResource("classpath:/import/amazon_books.csv").getInputStream();
        
        webClient.perform(fileUpload("/items/albums/upload")
                .file(new MockMultipartFile("csv", invalidCsv)))
                .andExpect(status().isInternalServerError());
    }
}
