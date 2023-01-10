package org.example;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EmfUtil {





    private EmfUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static List<URI> getPcmUris() {
        URI pcmUri = URI.createFileURI("./src/main/resources/pcm.ecore");
        URI identifierUri = URI.createFileURI("./src/main/resources/identifier.ecore");
        URI probabilityFunctionUri = URI.createFileURI("./src/main/resources/ProbabilityFunction.ecore");
        URI stoexUri = URI.createFileURI("./src/main/resources/stoex.ecore");
        URI unitsUri = URI.createFileURI("./src/main/resources/Units.ecore");

        return List.of(pcmUri, identifierUri, probabilityFunctionUri, stoexUri, unitsUri);
    }

    public static ResourceSet getResourceSet(List<URI> metamodelUris) {
        Resource.Factory.Registry resourceFactoryRegistry = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> extensionToFactoryMap = resourceFactoryRegistry.getExtensionToFactoryMap();
        extensionToFactoryMap.put(EcorePackage.eNAME, new XMIResourceFactoryImpl());
        extensionToFactoryMap.put("*", new XMIResourceFactoryImpl());


        ResourceSet rs = new ResourceSetImpl();
        // enable extended metadata
        final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
        rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA,
                extendedMetaData);

        for (var metamodelUri : metamodelUris) {
            Resource r = rs.getResource(metamodelUri, true);
            List<EPackage> metapackages = getPackageList(r);
            registerEPackageURIs(metapackages);
        }

        return rs;
    }

    private static List<EPackage> getPackageList(Resource r) {
        List<EPackage> packageList = r.getContents().stream().filter(EPackage.class::isInstance).map(EPackage.class::cast).toList();
        boolean changed = false;
        do {
            var extendedPackageList = new ArrayList<>(packageList);
            for (var ePackage : packageList) {
                for (var eSubPackage : ePackage.getESubpackages()) {
                    if (!packageList.contains(eSubPackage)) {
                        extendedPackageList.add(eSubPackage);
                    }
                }
            }
            var initialSize = packageList.size();
            var extendedSize = extendedPackageList.size();
            changed = initialSize < extendedSize;
            packageList = extendedPackageList;
        } while (changed);

        return packageList;
    }

    /**
     * Registers a collection of EPackages in the EPackage registry via their URIs.
     * @param ePackages are the EPackages to register.
     */
    public static void registerEPackageURIs(Collection<EPackage> ePackages) {
        ePackages.forEach(it -> EPackage.Registry.INSTANCE.put(it.getNsURI(), it));
    }

}
