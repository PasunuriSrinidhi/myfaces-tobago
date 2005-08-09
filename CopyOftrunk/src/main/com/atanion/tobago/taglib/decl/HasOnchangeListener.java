package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 3:14:01 PM
 * User: bommel
 * $Id$
 */
public interface HasOnchangeListener {

  /**
   * Clientside script function to add to this component's onchange handler.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setOnchange(String onchange);
}
