package nl._42.qualityws.cleancode._local.imdb;

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

}