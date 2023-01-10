package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static String mediastoreRepository = "./src/main/resources/ms.repository";

    public static void main(String[] args) {
        logger.info("Initializing...");
        EmfUtil.loadMetamodels(EmfUtil.getPcmUris());
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
        var c = resource.getContents().get(0);
        logger.info("{}", c);
    }

}
