package nl._42.qualityws.cleancode.collectors_item.service.imdb;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public interface ProxyServer {

    public ResponseEntity<String> call(RestTemplate restTemplate, String url, HttpEntity entity);

}