// Copyright 2006-2010, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$
package gov.nasa.pds.validate.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

/**
 * Class that provides support for handling namespaces in PDS4
 * data products
 *
 * @author mcayanan
 *
 */
public class PDSNamespaceContext implements NamespaceContext {
    private Map<String, String> namespaces;
    private String defaultNamespace;

    public PDSNamespaceContext(Namespace namespace) {
        this(namespace, null);
    }

    public PDSNamespaceContext(Namespace namespace,
            String defaultNamespace) {
        List<Namespace> list = new ArrayList<Namespace>();
        list.add(namespace);
        new PDSNamespaceContext(list, defaultNamespace);
    }

    public PDSNamespaceContext(List<Namespace> namespaces) {
        this(namespaces, null);
    }

    public PDSNamespaceContext(String defaultNamespace) {
        this(new ArrayList<Namespace>(), defaultNamespace);
    }

    public PDSNamespaceContext(List<Namespace> namespaces,
            String defaultNamespace) {
        this.namespaces = new HashMap<String, String>();
        for(Namespace ns : namespaces) {
            this.namespaces.put(ns.getPrefix(), ns.getUri());
        }
        this.defaultNamespace = defaultNamespace;
    }

    public String getDefaultNamepsace() {
        return defaultNamespace;
    }

    @Override
    public String getNamespaceURI(String prefix) {
        if(prefix == null || "".equals(prefix)) {
            return namespaces.get("pds");
        } else {
            return namespaces.get(prefix);
        }
    }

    @Override
    public String getPrefix(String arg0) {
        // Method not necessary
        return null;
    }

    @Override
    public Iterator getPrefixes(String arg0) {
        // Method not necessary
        return null;
    }

}
