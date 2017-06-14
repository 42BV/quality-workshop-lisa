package nl._42.qualityws.cleancode.shared.test;

import io.beanmapper.BeanMapper;
import nl._42.beanie.BeanBuilder;
import nl._42.beanie.save.BeanSaver;
import nl._42.beanie.save.JpaBeanSaver;
import nl._42.beanie.save.TransactionalBeanSaver;
import nl._42.qualityws.cleancode.shared.entity.AbstractEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TestConfig {
    
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Bean
    public BeanBuilder beanBuilder() {
        BeanBuilder beanBuilder = new BeanBuilder(new TransactionalBeanSaver(transactionManager, beanSaver()));
        beanBuilder.skip(AbstractEntity.class, "id");
        beanBuilder.setBeanMapper(beanMapper);
        return beanBuilder;
    }

    @Bean
    public BeanSaver beanSaver() {
        return new JpaBeanSaver();
    }
}
