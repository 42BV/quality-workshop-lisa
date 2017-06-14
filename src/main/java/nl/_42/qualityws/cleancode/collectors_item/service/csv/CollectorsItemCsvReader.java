package nl._42.qualityws.cleancode.collectors_item.service.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;

class CollectorsItemCsvReader<T extends CollectorsItemCsvRecord> {

    private final Class<T> csvRecordType;

    CollectorsItemCsvReader(Class<T> csvRecordType) {
        this.csvRecordType = csvRecordType;
    }

    List<T> read(InputStream items) {
        try(Reader reader = new InputStreamReader(items)) {
            CsvClient<T> client = new CsvClientImpl<>(reader, csvRecordType);
            return client.readBeans();
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

}
