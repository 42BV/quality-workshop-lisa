package nl._42.qualityws.cleancode.collector.service;

import org.springframework.data.jpa.repository.JpaRepository;

import nl._42.qualityws.cleancode.collector.Collector;

interface CollectorRepository extends JpaRepository<Collector, Long> {

    Collector findByName(String name);

}