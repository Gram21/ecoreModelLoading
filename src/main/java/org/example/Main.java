package org.example;

import org.eclipse.emf.common.util.URI;

import java.io.IOException;

public class Main {
    private static String mediastoreRepository = "./src/main/resources/ms.repository";
    private static String mediastoreUml = "./src/main/resources/ms.uml";
    public static void main(String[] args) {
        System.out.println("Hello world!");
        var rs = EmfUtil.getResourceSet();
        var resource = rs.getResource(URI.createFileURI(mediastoreRepository), true);
        try {
            resource.load(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var c = resource.getContents().get(0);
        System.out.println(c);
    }
}
