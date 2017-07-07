package nl._42.qualityws.cleancode.collector.controller;

import static io.beanmapper.spring.PageableMapper.map;

import javax.validation.Valid;

import io.beanmapper.BeanMapper;
import io.beanmapper.spring.web.MergedForm;
import nl._42.qualityws.cleancode.collector.Collector;
import nl._42.qualityws.cleancode.collector.service.CollectorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("collectors")
public class CollectorController {

    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private CollectorService collectorService;

    @GetMapping
    public Page<CollectorResult> list(@SortDefault("name") Pageable pageable) {
        return map(collectorService.list(pageable), CollectorResult.class, beanMapper);
    }
    
    @PostMapping
    public CollectorResult create(@Valid @MergedForm(value = CollectorForm.class) Collector collector) {
        return beanMapper.map(collectorService.create(collector), CollectorResult.class);
    }
}
