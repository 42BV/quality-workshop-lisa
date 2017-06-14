package nl._42.qualityws.cleancode.collectors_item.service;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.io.InputStream;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.beanmapper.BeanMapper;
import nl._42.qualityws.cleancode.collectors_item.Album;
import nl._42.qualityws.cleancode.collectors_item.Book;
import nl._42.qualityws.cleancode.collectors_item.CollectorsItem;
import nl._42.qualityws.cleancode.collectors_item.Movie;
import nl._42.qualityws.cleancode.collectors_item.service.csv.CollectorsItemCsvReaderFacade;

@Service
public class CollectorsItemService {

    protected final static Logger LOGGER = LoggerFactory.getLogger(CollectorsItemService.class);

    @Autowired
    private CollectorsItemRepository collectorsItemRepository;
    
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CollectorsItemCsvReaderFacade csvReader;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private MovieValidator movieValidator;

    @Autowired
    private AlbumValidator albumValidator;

    @Autowired
    private BookValidator bookValidator;

    public <T extends CollectorsItem> T create(T item) {
        notNull(item, "Collectors' item to create may not be null");
        isTrue(item.isNew(), "Cannot create existing collectors' item");
        return collectorsItemRepository.save(item);
    }

    public void importBooks(InputStream bookStream) {
        Collection<Book> books = csvReader.readBooks(bookStream);
        merge(bookValidator, books);
    }

    public void importMovies(InputStream movieStream) {
        Collection<Movie> movies = csvReader.readMovies(movieStream);
        merge(movieValidator, movies);
    }

    public void importAlbums(InputStream albumStream) {
        Collection<Album> albums = csvReader.readAlbums(albumStream);
        merge(albumValidator, albums);
    }

    private <T extends CollectorsItem> void merge(CollectorsItemValidator<T> validator, Collection<T> items) {
        for (T item : items) {
            if (!validator.validate(item)) {
                continue;
            }
            collectorsItemRepository.save(mergeItem(item));
        }
    }

    private CollectorsItem mergeItem(CollectorsItem item) {
        CollectorsItem foundItem = collectorsItemRepository.findByName(item.getName());
        if (foundItem == null) {
            return item;
        }
        LOGGER.info("Item already exists [{}], merging", item.getName());
        return beanMapper.map(item, foundItem);
    }

    public Page<Movie> listMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Page<Album> listAlbums(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }

    public Page<Book> listBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

}
