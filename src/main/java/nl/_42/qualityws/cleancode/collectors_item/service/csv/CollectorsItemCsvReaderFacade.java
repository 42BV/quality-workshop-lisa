package nl._42.qualityws.cleancode.collectors_item.service.csv;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import io.beanmapper.BeanMapper;
import nl._42.qualityws.cleancode.collectors_item.Album;
import nl._42.qualityws.cleancode.collectors_item.Book;
import nl._42.qualityws.cleancode.collectors_item.CollectorsItem;
import nl._42.qualityws.cleancode.collectors_item.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CollectorsItemCsvReaderFacade {

    @Autowired
    private BeanMapper beanMapper;

    public Collection<Movie> readMovies(InputStream items) {
        return read(items, MovieCsvRecord.class, Movie.class);
    }

    public Collection<Album> readAlbums(InputStream items) {
        return read(items, AlbumCsvRecord.class, Album.class);
    }

    public Collection<Book> readBooks(InputStream items) {
        return read(items, BookCsvRecord.class, Book.class);
    }

    private <C extends CollectorsItemCsvRecord, T extends CollectorsItem> Collection<T>
            read(
                InputStream items,
                Class<C> csvRecordClass,
                Class<T> entityClass) {
        CollectorsItemCsvReader<C> reader = new CollectorsItemCsvReader<>(csvRecordClass);
        List<C> bookRecords = reader.read(items);
        return beanMapper.map(bookRecords, entityClass);
    }

}
