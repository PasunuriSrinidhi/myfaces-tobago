package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:47:04 PM
 * User: bommel
 * $Id$
 */
public interface IsDisabled {
  /**
   *  Flag indicating that this element is disabled.
   */
  @TagAttribute(type=String.class)
  @UIComponentTagAttribute(type="java.lang.Boolean")
   void setDisabled(String disabled);
}
