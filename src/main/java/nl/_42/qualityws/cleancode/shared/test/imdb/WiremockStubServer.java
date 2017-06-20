package nl._42.qualityws.cleancode.shared.test.imdb;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import nl._42.qualityws.cleancode.collectors_item.service.imdb.ProxyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.WireMockServer;

public class WiremockStubServer implements ProxyServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(WiremockStubServer.class);

    private WireMockServer wireMockServer;

    private final Integer wiremockPort;

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

}