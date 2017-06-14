package nl._42.qualityws.cleancode.shared;

import nl._42.database.truncator.DatabaseTruncator;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractIntegrationTest {

    @Autowired
    private DatabaseTruncator truncator;
    
    @After
    public void cleanUp() throws Exception {
        truncator.truncate();
    }
}