/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 26.08.2004 16:25:39.
 * $Id: MockVariableResolver.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package com.atanion.tobago.mock.faces;

import javax.faces.el.VariableResolver;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import java.util.Map;

/**
 * <p>Mock implementation of {@link VariableResolver} that supports a limited
 * subset of expression evaluation functionality:</p>
 * <ul>
 * <li>Recognizes <code>applicationScope</code>, <code>requestScope</code>,
 *     and <code>sessionScope</code> implicit names.</li>
 * <li>Searches in ascending scopes for non-reserved names.</li>
 * </ul>
 */

public class MockVariableResolver extends VariableResolver {


    // ------------------------------------------------------------ Constructors


    // ------------------------------------------------ VariableResolver Methods


    public Object resolveVariable(FacesContext context, String name) {

        if ((context == null) || (name == null)) {
            throw new NullPointerException();
        }

        // Handle predefined variables
        if ("applicationScope".equals(name)) {
            return (econtext().getApplicationMap());
        } else if ("requestScope".equals(name)) {
            return (econtext().getRequestMap());
        } else if ("sessionScope".equals(name)) {
            return (econtext().getSessionMap());
        }

        // Look up in ascending scopes
        Map map = null;
        map = econtext().getRequestMap();
        if (map.containsKey(name)) {
            return (map.get(name));
        }
        map = econtext().getSessionMap();
        if ((map != null) && (map.containsKey(name))) {
            return (map.get(name));
        }
        map = econtext().getApplicationMap();
        if (map.containsKey(name)) {
            return (map.get(name));
        }

        // Requested object is not found
        return (null);

    }



    // --------------------------------------------------------- Private Methods


    private ExternalContext econtext() {

        return (FacesContext.getCurrentInstance().getExternalContext());

    }


}
