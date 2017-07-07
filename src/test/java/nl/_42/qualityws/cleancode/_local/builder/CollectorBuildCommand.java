package nl._42.qualityws.cleancode._local.builder;

import nl._42.beanie.EditableBeanBuildCommand;
import nl._42.qualityws.cleancode.collector.Collector;

public interface CollectorBuildCommand extends EditableBeanBuildCommand<Collector> {

    CollectorBuildCommand withName(String name);
}
