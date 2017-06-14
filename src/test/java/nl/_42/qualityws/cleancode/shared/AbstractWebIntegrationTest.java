package nl._42.qualityws.cleancode.shared;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractWebIntegrationTest extends AbstractIntegrationTest {

    protected MockMvc webClient;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Before
    public void initWebClient() {
        DefaultMockMvcBuilder webClientBuilder = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(log());

        this.webClient = webClientBuilder
                .defaultRequest(get("/").contentType(APPLICATION_JSON))
                .build();
    }

}