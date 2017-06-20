package nl._42.qualityws.cleancode.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "collector.imdb")
public class CollectorProperties {

    // +---------+---------------------------------+
    // | default | use WM URL, start WM, stub mode |
    // | test    | use WM URL, start WM, stub mode |
    // | imdb    | use own URL,no WM               |
    // | record  | use WM URL ,no WM               |
    // +---------+---------------------------------+

    private String imdbUrl;

    private String wiremockPort;

    private Boolean startWiremockServer;

    public String getImdbUrl() {
        return imdbUrl;
    }

    public void setImdbUrl(String imdbUrl) {
        this.imdbUrl = imdbUrl;
    }

    public String getWiremockPort() {
        return wiremockPort;
    }

    public void setWiremockPort(String wiremockPort) {
        this.wiremockPort = wiremockPort;
    }

    public Boolean getStartWiremockServer() {
        return startWiremockServer;
    }

    public void setStartWiremockServer(Boolean startWiremockServer) {
        this.startWiremockServer = startWiremockServer;
    }

    public String determineUrlToCall(String movieUrl) {
        if (wiremockPort == null || wiremockPort.equals("")) {
            return movieUrl;
        }
        try {
            URL url = new URL(movieUrl);
            return "http://localhost:" + wiremockPort + url.getPath();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}