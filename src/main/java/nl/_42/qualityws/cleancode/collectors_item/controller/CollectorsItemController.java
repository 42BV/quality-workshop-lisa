package nl._42.qualityws.cleancode.collectors_item.controller;

import static io.beanmapper.spring.PageableMapper.map;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.beanmapper.BeanMapper;
import io.beanmapper.spring.web.MergedForm;
import nl._42.qualityws.cleancode.collectors_item.Album;
import nl._42.qualityws.cleancode.collectors_item.Book;
import nl._42.qualityws.cleancode.collectors_item.Movie;
import nl._42.qualityws.cleancode.collectors_item.service.CollectorsItemService;

@RestController
@RequestMapping("items")
public class CollectorsItemController {

    @Autowired
    private CollectorsItemService itemService;

    @Autowired
    private BeanMapper beanMapper;
    
    @GetMapping("/movies")
    public Page<MovieResult> listMovies(@SortDefault({"collector.name", "name"}) Pageable pageable) {
        return map(itemService.listMovies(pageable), MovieResult.class, beanMapper);
    }
    
    @GetMapping("/albums")
    public Page<AlbumResult> listAlbums(@SortDefault({"collector.name", "name"}) Pageable pageable) {
        return map(itemService.listAlbums(pageable), AlbumResult.class, beanMapper);
    }
    
    @GetMapping("/books")
    public Page<BookResult> listBooks(@SortDefault({"collector.name", "name"}) Pageable pageable) {
        return map(itemService.listBooks(pageable), BookResult.class, beanMapper);
    }
    
    @PostMapping(path = "/movies")
    public MovieResult createMovie(@Valid @MergedForm(value = MovieForm.class) Movie movie) {
        return beanMapper.map(itemService.create(movie), MovieResult.class);
    }

    @PostMapping(path = "/albums")
    public AlbumResult createAlbum(@Valid @MergedForm(value = AlbumForm.class) Album album) {
        return beanMapper.map(itemService.create(album), AlbumResult.class);
    }

    @PostMapping(path = "/books")
    public BookResult createBook(@Valid @MergedForm(value = BookForm.class) Book book) {
        return beanMapper.map(itemService.create(book), BookResult.class);
    }

    @PostMapping("/movies/upload")
    public void uploadMovies(@RequestPart MultipartFile csv) throws IOException {
        itemService.importMovies(csv.getInputStream());
    }
    
    @PostMapping("/albums/upload")
    public void uploadAlbums(@RequestPart MultipartFile csv) throws IOException {
        itemService.importAlbums(csv.getInputStream());
    }
    
    @PostMapping("/books/upload")
    public void uploadBooks(@RequestPart MultipartFile csv) throws IOException {
        itemService.importBooks(csv.getInputStream());
    }
}
