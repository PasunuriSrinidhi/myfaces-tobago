/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUISegmentLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

/**
 * Renders a layout using a 12 columns grid.
 * Find more information on how the grid works in the Twitter Bootstrap documentation.
 *
 * If no attribute is defined, extraSmall="12" will be used as default.
 * @since 3.0.0
 */
@Tag(name = "segmentLayout")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISegmentLayout",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISegmentLayout",
    componentFamily = AbstractUISegmentLayout.COMPONENT_FAMILY,
    rendererType = RendererTypes.SEGMENT_LAYOUT,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Majorra.
        "javax.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponenents = "NONE")
public interface SegmentLayoutTagDeclaration extends HasIdBindingAndRendered, IsVisual {

  /**
   * The semicolon-separated definition of the columns for extra small devices. The sum must be 12.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.ColumnPartition")
  void setExtraSmall(String extraSmall);

  /**
   * The semicolon-separated definition of the columns for small devices. The sum must be 12.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.ColumnPartition")
  void setSmall(String small);

  /**
   * The semicolon-separated definition of the columns for medium devices. The sum must be 12.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.ColumnPartition")
  void setMedium(String medium);

  /**
   * The semicolon-separated definition of the columns for large devices. The sum must be 12.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.ColumnPartition")
  void setLarge(String large);

}
