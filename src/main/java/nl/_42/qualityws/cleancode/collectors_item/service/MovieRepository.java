package nl._42.qualityws.cleancode.collectors_item.service;

import nl._42.qualityws.cleancode.collectors_item.Movie;

import org.springframework.data.jpa.repository.JpaRepository;

interface MovieRepository extends JpaRepository<Movie, Long> {

}
