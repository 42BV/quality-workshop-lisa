package nl._42.qualityws.cleancode.collectors_item.service.csv;

import io.beanmapper.annotations.BeanProperty;

import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvFile;
import org.csveed.bean.ColumnNameMapper;

@CsvFile(separator = ',', mappingStrategy = ColumnNameMapper.class)
public class MovieCsvRecord implements CollectorsItemCsvRecord {

    @CsvCell(columnName = "collector_name")
    private String collector;
    @CsvCell
    @BeanProperty(name = "imdbUrl")
    private String url;
    @CsvCell
    private String name;

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
