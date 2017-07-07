package nl._42.qualityws.cleancode._local.imdb;

import nl._42.qualityws.cleancode.collectors_item.service.imdb.ProxyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CollectorProperties.class)
public class WiremockConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(WiremockConfiguration.class);

    private final CollectorProperties properties;

    public WiremockConfiguration(CollectorProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnProperty(name="collector.imdb.startWiremockServer")
    public ProxyServer imdbMockServer() {
        LOGGER.info("Creating WiremockStubServer on port {}", properties.getWiremockPort());
        return new WiremockStubServer(properties.getWiremockPort());
    }

}