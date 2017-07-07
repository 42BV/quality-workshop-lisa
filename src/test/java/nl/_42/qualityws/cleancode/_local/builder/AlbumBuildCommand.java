package nl._42.qualityws.cleancode._local.builder;

import nl._42.beanie.EditableBeanBuildCommand;
import nl._42.qualityws.cleancode.collector.Collector;
import nl._42.qualityws.cleancode.collectors_item.Album;

public interface AlbumBuildCommand extends EditableBeanBuildCommand<Album> {
    
    AlbumBuildCommand withName(String name);
    AlbumBuildCommand withCollector(Collector collector);
    AlbumBuildCommand withSpotifyUrl(String spotifyUrl);
    AlbumBuildCommand withArtist(String artist);
}
