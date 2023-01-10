package org.example;

import org.eclipse.emf.common.util.URI;

import java.io.IOException;

public class Main {
    private static String mediastoreRepository = "./src/main/resources/ms.repository";

    public static void main(String[] args) {
        System.out.println("Initializing...");
        var rs = EmfUtil.getResourceSet(EmfUtil.getPcmUris());
        System.out.println("Loading Model...");
        var resource = rs.getResource(URI.createFileURI(mediastoreRepository), true);
        System.out.println("Initializing and loading done!");
        try {
            resource.load(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var c = resource.getContents().get(0);
        System.out.println(c);
    }
}
