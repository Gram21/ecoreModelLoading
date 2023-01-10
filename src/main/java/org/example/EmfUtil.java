package org.example;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.util.Map;

public class EmfUtil {
    private static URI pcmUri = URI.createFileURI("./src/main/resources/pcm.ecore");
    private static URI umlUri = URI.createFileURI("./src/main/resources/UML.ecore");
    private static URI idUri = URI.createFileURI("./src/main/resources/identifier.ecore");

    private EmfUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static ResourceSet getResourceSet() {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
                "ecore", new EcoreResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

        ResourceSet rs = new ResourceSetImpl();
        // enable extended metadata
        final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
        rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA,
                extendedMetaData);

        Resource r = rs.getResource(pcmUri, true);
        EObject eObject = r.getContents().get(0);
        if (eObject instanceof EPackage) {
            EPackage ePackage = (EPackage)eObject;
            EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
            for (var subPackage : ePackage.getESubpackages()) {
                EPackage.Registry.INSTANCE.put(subPackage.getNsURI(), subPackage);
            }
        }

        r = rs.getResource(umlUri, true);
        eObject = r.getContents().get(0);
        if (eObject instanceof EPackage) {
            EPackage ePackage = (EPackage)eObject;
            EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
        }

        r = rs.getResource(idUri, true);
        eObject = r.getContents().get(0);
        if (eObject instanceof EPackage) {
            EPackage ePackage = (EPackage)eObject;
            EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
        }

        return rs;
    }

}
