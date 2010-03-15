package org.apache.myfaces.tobago.taglib.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasTreeNodeValue;
import org.apache.myfaces.tobago.taglib.decl.IsGridLayoutComponent;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

/**
 * A tree which will be displayed like a flat menu.
 * This menu is often used for navigation on the left side of an application.
 */
@Tag(name = "treeMenu")
@BodyContentDescription(anyTagOf = "<tc:treeNode>|<tc:treeData>")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITreeMenu",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUITree",
    rendererType = RendererTypes.TREE_MENU,
    allowedChildComponenents = {
        "org.apache.myfaces.tobago.TreeNode",
        "org.apache.myfaces.tobago.TreeData"
        })
public interface TreeMenuTagDeclaration
    extends HasIdBindingAndRendered, HasTreeNodeValue, IsRequired, IsGridLayoutComponent, HasMarkup {

  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setShowRoot(String showRoot);
  
  /**
   *
   * <strong>ValueBindingExpression</strong> pointing to a object to save the
   * component's state.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Object", expression = DynamicExpression.VALUE_BINDING_REQUIRED)
  void setState(String state);
}
