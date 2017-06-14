package nl._42.qualityws.cleancode.collectors_item.service;

import nl._42.qualityws.cleancode.collectors_item.Album;

import org.springframework.data.jpa.repository.JpaRepository;

interface AlbumRepository extends JpaRepository<Album, Long> {

}
