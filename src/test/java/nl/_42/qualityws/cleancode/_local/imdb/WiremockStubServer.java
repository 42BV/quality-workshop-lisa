package nl._42.qualityws.cleancode._local.imdb;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import nl._42.qualityws.cleancode.collectors_item.service.imdb.DefaultProxyServer;
import nl._42.qualityws.cleancode.collectors_item.service.imdb.ProxyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.WireMockServer;

public class WiremockStubServer implements ProxyServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(WiremockStubServer.class);

    private WireMockServer wireMockServer;

    private final Integer wiremockPort;

    private final ProxyServer defaultProxyServer = new DefaultProxyServer();

    public WiremockStubServer(String wiremockPort) {
        this.wiremockPort = Integer.parseInt(wiremockPort);
    }

    @PostConstruct
    public void startWiremock() {
        LOGGER.info("Starting WireMock server");
        wireMockServer = new WireMockServer(options().port(wiremockPort));
        wireMockServer.start();
    }

    @PreDestroy
    public void stopWiremock() {
        LOGGER.info("Ending WireMock server");
        wireMockServer.stop();
    }

    @Override
    public ResponseEntity<String> call(RestTemplate restTemplate, String url, HttpEntity entity) {
        return defaultProxyServer.call(
                restTemplate,
                determineUrlToCall(url),
                entity);
    }

    private String determineUrlToCall(String url) {
        if (wiremockPort == null || wiremockPort.equals("")) {
            LOGGER.info("Use original URL {}", url);
            return url;
        }
        try {
            String useUrl = "http://localhost:" + wiremockPort + (new URL(url)).getPath();
            LOGGER.info("Original URL {}, used URL {}", url, useUrl);
            return useUrl;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}