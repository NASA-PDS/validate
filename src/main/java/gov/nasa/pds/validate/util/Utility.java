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

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Utility class.
 *
 * @author mcayanan
 *
 */
public class Utility {
    /**
     * Removes quotes within a list of strings.
     *
     * @param list A list of strings.
     * @return A list with the quotes removed.
     */
    public static List<String> removeQuotes(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).toString().replace('"', ' ')
                    .trim());
        }
        return list;
    }

    public static String toStringNoBraces(JsonObject json) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      StringBuilder string = new StringBuilder(gson.toJson(json));
      string = string.replace(0, 1,"");
      string = string.replace(string.lastIndexOf("}"), string.lastIndexOf("}")+1, "");
      return string.toString().trim();
    }
}
