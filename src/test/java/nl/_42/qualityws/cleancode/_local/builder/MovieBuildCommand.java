package nl._42.qualityws.cleancode._local.builder;

import nl._42.beanie.EditableBeanBuildCommand;
import nl._42.qualityws.cleancode.collector.Collector;
import nl._42.qualityws.cleancode.collectors_item.Movie;

public interface MovieBuildCommand extends EditableBeanBuildCommand<Movie> {
    
    MovieBuildCommand withName(String name);
    MovieBuildCommand withCollector(Collector collector);
    MovieBuildCommand withImdbUrl(String imdbUrl);
}
