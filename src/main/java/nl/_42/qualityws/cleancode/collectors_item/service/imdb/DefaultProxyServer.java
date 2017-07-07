package nl._42.qualityws.cleancode.collectors_item.service.imdb;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DefaultProxyServer implements ProxyServer {

    @Override
    public ResponseEntity<String> call(RestTemplate restTemplate, String url, HttpEntity entity) {
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

}
