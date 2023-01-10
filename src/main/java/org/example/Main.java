package org.example;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static String mediastoreRepository = "./src/main/resources/ms.repository";
    private static String umlRepository = "./src/main/resources/ms.uml";

    public static void main(String[] args) {
        logger.info("Initializing...");
        EmfUtil.loadMetamodels(EmfUtil.getPcmUris());
        EmfUtil.loadMetamodels(EmfUtil.getUmlUris());
        logger.info("Loading Model...");
        var resource = EmfUtil.loadResource(new File(mediastoreRepository));
        if (resource == null) {
            logger.error("Unsuccessful loading, aborting!");
            return;
        }

        logger.info("Initializing and loading done!");
        try {
            resource.load(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var repository = resource.getContents().get(0);
        logger.info("{}", repository);

        List<String> validTypes = List.of("BasicComponent", "CompositeComponent", "OperationInterface");
        for (String type : validTypes) {
            var objectsOfType = getObjectsOfType(repository, type);
            logger.info("#{}:{}", type, objectsOfType.size());
            var names = getAllNames(objectsOfType);
            names.forEach(it -> logger.info("{}", it));
        }
    }

    private static List<EObject> getObjectsOfType(EObject repository, String type) {
        return repository.eContents().stream().filter(it -> it.eClass().getName().equals(type)).toList();
    }

    private static List<String> getAllNames(List<EObject> objects) {
        if (objects.size() == 0) {
            return new ArrayList<>();
        }
        var entityNameFeature = objects.get(0).eClass().getEStructuralFeature("entityName");
        return objects.stream().map(it -> it.eGet(entityNameFeature).toString()).toList();
    }

}
