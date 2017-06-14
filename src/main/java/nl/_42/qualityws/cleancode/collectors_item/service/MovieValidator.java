package nl._42.qualityws.cleancode.collectors_item.service;

import java.util.ArrayList;
import java.util.List;

import nl._42.qualityws.cleancode.collectors_item.Movie;

import org.springframework.stereotype.Component;

import com.sun.javafx.binding.StringFormatter;

@Component
class MovieValidator extends AbstractCollectorsItemValidator<Movie> {

    @Override
    protected List<ValidationError> validateItem(Movie movie) {
        final List<ValidationError> errors = new ArrayList<>();
        if (!movie.getImdbUrl().startsWith("http://www.imdb.com/title/")) {
            errors.add(new ValidationError(StringFormatter.format("illegal IMDB URL %s", movie.getImdbUrl()).getValue()));
        }
        return errors;
    }

    @Override
    protected void logErrorHeader(Movie movie) {
        LOGGER.error("Errors for movie [{}]", movie.getName());
    }

}
