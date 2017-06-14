package nl._42.qualityws.cleancode.collectors_item.service;

import nl._42.qualityws.cleancode.collectors_item.Book;

import org.springframework.data.jpa.repository.JpaRepository;

interface BookRepository extends JpaRepository<Book, Long> {

}
