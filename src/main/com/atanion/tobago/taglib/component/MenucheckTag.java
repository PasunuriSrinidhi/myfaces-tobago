/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:49:33.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class MenucheckTag extends MenuradioTag {
// ----------------------------------------------------------------- attributes

  private String labelWithAccessKey;


// ----------------------------------------------------------- business methods

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_MENU_TYPE, "menuCheck", getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
  }

  public void release() {
    super.release();
    labelWithAccessKey = null;
  }

// ------------------------------------------------------------ getter + setter
  public String getLabelWithAccessKey() {
    return labelWithAccessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }
}