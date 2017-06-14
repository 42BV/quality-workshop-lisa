package nl._42.qualityws.cleancode.collector.controller;

import org.hibernate.validator.constraints.NotEmpty;

public class CollectorForm {

    @NotEmpty
    public String name;
}
