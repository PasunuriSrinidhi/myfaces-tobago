/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:49:33.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class MenuitemTag extends MenuradioTag {
// ----------------------------------------------------------------- attributes

  private String image;
  private String labelWithAccessKey;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    image = null;
    labelWithAccessKey = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_MENU_TYPE, "menuItem", getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getLabelWithAccessKey() {
    return labelWithAccessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }
}