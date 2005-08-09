/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 09.01.2004 11:57:24.
 * $Id$
 */
package com.atanion.tobago.el;

import javax.faces.el.MethodBinding;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class ConstantMethodBinding
    extends MethodBinding implements Serializable {

  private String outcome;

  public ConstantMethodBinding(String outcome) {
    this.outcome = outcome;
  }

  // todo: check if needed, in the moment this is needed for MyFaces state saving 
  public ConstantMethodBinding() {
  }

  public Object invoke(FacesContext facescontext, Object aobj[])
      throws EvaluationException, MethodNotFoundException {
    return outcome;
  }

  public Class getType(FacesContext facescontext)
      throws MethodNotFoundException {
    return String.class;
  }

  public String getExpressionString() {
    return outcome;
  }
}
