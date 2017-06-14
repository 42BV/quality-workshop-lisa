package nl._42.qualityws.cleancode.collectors_item.service;

import nl._42.qualityws.cleancode.collectors_item.CollectorsItem;

import org.springframework.data.jpa.repository.JpaRepository;

interface CollectorsItemRepository extends JpaRepository<CollectorsItem, Long> {

    CollectorsItem findByName(String name);

}
