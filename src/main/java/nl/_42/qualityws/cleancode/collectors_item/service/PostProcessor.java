package nl._42.qualityws.cleancode.collectors_item.service;

import nl._42.qualityws.cleancode.collectors_item.CollectorsItem;

public interface PostProcessor<T extends CollectorsItem> {

    void process(CollectorsItem item);

}
