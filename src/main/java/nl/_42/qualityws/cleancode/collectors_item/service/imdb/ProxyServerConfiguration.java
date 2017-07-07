package nl._42.qualityws.cleancode.collectors_item.service.imdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyServerConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProxyServerConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(ProxyServer.class)
    public ProxyServer defaultProxyServer() {
        LOGGER.info("No ProxyServer found. Creating default, pass-through ImdbProxyServer");
        return new DefaultProxyServer();
    }

}
