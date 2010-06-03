package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.CreateComponentUtils;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.component.UISelectOneCommand;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.component.UIToolBarSeparator;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.UICommandBase;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public abstract class ToolBarRendererBase extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ToolBarRendererBase.class);

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    HtmlRendererUtils.renderDojoDndSource(facesContext, component);
  }

  protected String getLabelPosition(UIComponent component) {
    return (String) component.getAttributes().get(Attributes.LABEL_POSITION);
  }

  protected String getIconSize(UIComponent component) {
    return (String) component.getAttributes().get(Attributes.ICON_SIZE);
  }

  protected boolean isRightAligned(UIToolBar toolBar) {
    return UIToolBar.ORIENTATION_RIGHT.equals(toolBar.getOrientation());
  }

  @Override
  public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    UIToolBar toolBar = (UIToolBar) component;

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(context);

    Measure width = Measure.valueOf(-1);
    for (UIComponent command : (List<UIComponent>)toolBar.getChildren()) {
      if (command instanceof UICommandBase) {
        width = renderToolbarCommand(context, toolBar, (UICommandBase) command, writer, width);
      } else if (command instanceof UIToolBarSeparator) {
        width = renderSeparator(context, toolBar, (UIToolBarSeparator) command, writer, width);
      } else {
        LOG.error("Illegal UIComponent class in toolbar (not a UICommandBase):" + command.getClass().getName());
      }
    }
  }

  private Measure renderToolbarCommand(
      FacesContext facesContext, UIToolBar toolBar, UICommandBase command, TobagoResponseWriter writer, Measure width)
      throws IOException {
    if (command instanceof UISelectBooleanCommand) {
      return renderSelectBoolean(facesContext, toolBar, command, writer, width);
    } else if (command instanceof UISelectOneCommand) {
      return renderSelectOne(facesContext, toolBar, command, writer, width);
    } else {
      if (command.getFacet(Facets.RADIO) != null) {
        return renderSelectOne(facesContext, toolBar, command, writer, width);
      } else if (command.getFacet(Facets.CHECKBOX) != null) {
        return renderSelectBoolean(facesContext, toolBar, command, writer, width);
      } else {
        String onCommandClick = createCommandOnClick(facesContext, command);
        String onMenuClick = createMenuOnClick(command);
        return renderToolbarButton(
            facesContext, toolBar, command, writer, false, onCommandClick, onMenuClick, width);
      }
    }
  }

  // todo: remove component creation in renderer, for JSF 2.0
  // todo: One solution is to make <tx:toolBarSelectOne> instead of <tc:toolBarSelectOne>
  private Measure renderSelectOne(
      FacesContext facesContext, UIToolBar toolBar, UICommandBase command, TobagoResponseWriter writer, Measure width)
      throws IOException {

    String onclick = createCommandOnClick(facesContext, command);

    List<SelectItem> items;

    UIMenuSelectOne radio = (UIMenuSelectOne) command.getFacet(Facets.RADIO);
    if (radio == null) {
      items = RenderUtils.getSelectItems(command);
      radio = CreateComponentUtils.createUIMenuSelectOneFacet(facesContext, command);
      radio.setId(facesContext.getViewRoot().createUniqueId());
    } else {
      items = RenderUtils.getSelectItems(radio);
    }

    if (radio != null) {
      Object value = radio.getValue();

      boolean markFirst = !ComponentUtils.hasSelectedValue(items, value);
      String radioId = radio.getClientId(facesContext);
      String onClickPrefix = "tobago_toolBarSetRadioValue('" + radioId + "', '";
      String onClickPostfix = onclick != null ? "') ; " + onclick : "";
      for (SelectItem item : items) {
        final String labelText = item.getLabel();
        if (labelText != null) {
          command.getAttributes().put(Attributes.LABEL, labelText);
        } else {
          LOG.warn("Menu item has label=null. UICommand.getClientId()=" + command.getClientId(facesContext));
        }

        String image = null;
        if (item instanceof org.apache.myfaces.tobago.model.SelectItem) {
          image = ((org.apache.myfaces.tobago.model.SelectItem) item).getImage();
        } else if (LOG.isDebugEnabled()) {
          LOG.debug("select item is not " + org.apache.myfaces.tobago.model.SelectItem.class.getName());
        }
        if (image == null) {
          image = "image/1x1.gif";
        }
        command.getAttributes().put(Attributes.IMAGE, image);

        if (item.getDescription() != null) {
          command.getAttributes().put(Attributes.TIP, item.getDescription());
        }

        String formattedValue = RenderUtils.getFormattedValue(facesContext, radio, item.getValue());
        onclick = onClickPrefix + formattedValue + onClickPostfix;
        final boolean checked;
        if (item.getValue().equals(value) || markFirst) {
          checked = true;
          markFirst = false;
          writer.writeJavascript("    " + onClickPrefix + formattedValue + "');");
        } else {
          checked = false;
        }

        width = renderToolbarButton(facesContext, toolBar, command, writer, checked, onclick, null, width);
      }
    }
    return width;
  }

  // todo: remove component creation in renderer, for JSF 2.0
  // todo: One solution is to make <tx:toolBarCheck> instead of <tc:toolBarCheck>
  // may be renamed to toolBarSelectBoolean?
  private Measure renderSelectBoolean(
      FacesContext facesContext, UIToolBar toolBar, UICommandBase command, TobagoResponseWriter writer, Measure width)
      throws IOException {

    UIComponent checkbox = command.getFacet(Facets.CHECKBOX);
    if (checkbox == null) {
      checkbox = CreateComponentUtils.createUISelectBooleanFacetWithId(facesContext, command);
    }

    final boolean checked = ComponentUtils.getBooleanAttribute(checkbox, Attributes.VALUE);
    final String clientId = checkbox.getClientId(facesContext);

    String onClick = createCommandOnClick(facesContext, command);
    onClick = "tobago_toolBarCheckToggle('" + clientId + "');" + (onClick != null ? onClick : "");

    if (checked) {
      // to initialize the client state
      writer.writeJavascript("    tobago_toolBarCheckToggle('" + clientId + "');\n");
    }

    return renderToolbarButton(facesContext, toolBar, command, writer, checked, onClick, null, width);
  }


  private Measure renderToolbarButton(
      FacesContext facesContext, UIToolBar toolBar, UICommandBase command, TobagoResponseWriter writer,
      boolean selected, String commandClick, String menuClick, Measure width)
      throws IOException {
    if (!command.isRendered()) {
      return width;
    }

    final String clientId = command.getClientId(facesContext);
    final boolean disabled = ComponentUtils.getBooleanAttribute(command, Attributes.DISABLED);
    final LabelWithAccessKey label = new LabelWithAccessKey(command);
    final UIComponent popupMenu = command.getFacet(Facets.MENUPOPUP);
    final ResourceManager resources = getResourceManager();

    final String labelPosition = getLabelPosition(command.getParent());
    final String iconSize = getIconSize(command.getParent());
    final String iconName = (String) command.getAttributes().get(Attributes.IMAGE);
    final boolean lackImage = iconName == null;
    final String image = lackImage ? null : getImage(facesContext, iconName, iconSize, disabled, selected);

    final boolean showIcon = !UIToolBar.ICON_OFF.equals(iconSize);
    final boolean iconBig = UIToolBar.ICON_BIG.equals(iconSize);

    final boolean showLabelBottom = UIToolBar.LABEL_BOTTOM.equals(labelPosition);
    final boolean showLabelRight = UIToolBar.LABEL_RIGHT.equals(labelPosition);
    final boolean showLabel = showLabelBottom || showLabelRight;
    // two separate buttons for the command and the sub menu
    final boolean separateButtons = hasAnyCommand(command) && popupMenu != null;

    final Measure paddingTop = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-top");
    final Measure paddingMiddle = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-middle");
    final Measure paddingBottom = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-bottom");
    final Measure paddingLeft = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-left");
    final Measure paddingCenter = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-center");
    final Measure paddingRight = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-right");
    final Measure iconBigHeight = resources.getThemeMeasure(facesContext, toolBar, "custom.icon-big-height");
    final Measure iconSmallHeight = resources.getThemeMeasure(facesContext, toolBar, "custom.icon-small-height");
    final Measure iconBigWidth = resources.getThemeMeasure(facesContext, toolBar, "custom.icon-big-width");
    final Measure iconSmallWidth = resources.getThemeMeasure(facesContext, toolBar, "custom.icon-small-width");

    // label style
    final Style labelStyle;
    if (showLabel) {
      labelStyle = new Style();
      labelStyle.setLeft(paddingLeft);
      labelStyle.setTop(paddingTop);
      labelStyle.setWidth(RenderUtils.calculateStringWidth(facesContext, toolBar, label.getText()));
      labelStyle.setHeight(resources.getThemeMeasure(facesContext, toolBar, "custom.label-height"));
    } else {
      labelStyle = null;
    }

    // button style
    final Style buttonStyle = new Style();
    buttonStyle.setLeft(Measure.ZERO);
    buttonStyle.setTop(Measure.ZERO);
    buttonStyle.setWidth(paddingLeft.add(paddingRight));
    buttonStyle.setHeight(paddingBottom.add(paddingTop));

    // icon style
    final Style iconStyle;

    if (showIcon) {
      iconStyle = new Style();
      iconStyle.setLeft(paddingLeft);
      iconStyle.setTop(paddingTop);
      iconStyle.setHeight(iconBig ? iconBigHeight : iconSmallHeight);
      if (lackImage && showLabelRight && StringUtils.isNotBlank(label.getText())) {
        iconStyle.setWidth(Measure.valueOf(1));
      } else {
        iconStyle.setWidth(iconBig ? iconBigWidth : iconSmallWidth);
      }
      if (showLabelBottom) {
        labelStyle.setTop(labelStyle.getTop().add(iconStyle.getHeight()).add(paddingMiddle));
        if (labelStyle.getWidth().lessThan(iconStyle.getWidth())) {
          // label smaller than icon
          labelStyle.setLeft(labelStyle.getLeft().add(iconStyle.getWidth().subtract(labelStyle.getWidth()).divide(2)));
          buttonStyle.setWidth(buttonStyle.getWidth().add(iconStyle.getWidth()));
        } else {
          // label bigger than icon
          iconStyle.setLeft(iconStyle.getLeft().add(labelStyle.getWidth().subtract(iconStyle.getWidth()).divide(2)));
          buttonStyle.setWidth(buttonStyle.getWidth().add(labelStyle.getWidth()));
        }
        buttonStyle.setHeight(
            buttonStyle.getHeight().add(iconStyle.getHeight()).add(paddingMiddle).add(labelStyle.getHeight()));
      } else if (showLabelRight) {
        labelStyle.setTop(labelStyle.getTop().add(iconStyle.getHeight().subtract(labelStyle.getHeight()).divide(2)));
        labelStyle.setLeft(labelStyle.getLeft().add(iconStyle.getWidth()).add(paddingCenter));
        buttonStyle.setWidth(
            buttonStyle.getWidth().add(iconStyle.getWidth()).add(paddingCenter).add(labelStyle.getWidth()));
        buttonStyle.setHeight(buttonStyle.getHeight().add(iconStyle.getHeight()));
      } else {
        buttonStyle.setWidth(buttonStyle.getWidth().add(iconStyle.getWidth()));
        buttonStyle.setHeight(buttonStyle.getHeight().add(iconStyle.getHeight()));
      }
    } else {
      iconStyle = null;
      if (showLabel) {
        // only label
        buttonStyle.setWidth(buttonStyle.getWidth().add(labelStyle.getWidth()));
        if (StringUtils.isBlank(label.getText())) {
          buttonStyle.setWidth(buttonStyle.getWidth().add(iconSmallWidth));
        }
        buttonStyle.setHeight(buttonStyle.getHeight().add(labelStyle.getHeight()));
      } else {
        // both off: use some reasonable defaults
        buttonStyle.setWidth(buttonStyle.getWidth().add(iconSmallWidth));
        buttonStyle.setHeight(buttonStyle.getHeight().add(iconSmallWidth));
      }
    }

    // opener style (for menu popup)
    final Style openerStyle = new Style();
    openerStyle.setWidth(resources.getThemeMeasure(facesContext, toolBar, "custom.opener-width"));
    openerStyle.setHeight(resources.getThemeMeasure(facesContext, toolBar, "custom.opener-height"));

    final Style menuStyle = new Style();
    menuStyle.setLeft(buttonStyle.getWidth());
    menuStyle.setTop(Measure.ZERO);
    menuStyle.setWidth(paddingLeft.add(openerStyle.getWidth()).add(paddingRight));
    menuStyle.setHeight(buttonStyle.getHeight());

    // opener style (for menu popup)
    openerStyle.setLeft(menuStyle.getWidth().subtract(openerStyle.getWidth()).divide(2));
    openerStyle.setTop(menuStyle.getHeight().subtract(openerStyle.getHeight()).divide(2));

    // item style
    final Style itemStyle = new Style();
    if (isRightAligned(toolBar)) { // overrides the default in the CSS file.
      itemStyle.setLeft(resources.getThemeMeasure(facesContext, toolBar, "css.border-right-width"));
    }
    itemStyle.setWidth(popupMenu != null ? buttonStyle.getWidth().add(menuStyle.getWidth()) : buttonStyle.getWidth());
    itemStyle.setHeight(buttonStyle.getHeight());

    // change values when only have one button
    if (popupMenu != null && !separateButtons && (!lackImage || StringUtils.isNotBlank(label.getText()))) {
      openerStyle.setLeft(openerStyle.getLeft().add(buttonStyle.getWidth()));
      buttonStyle.setWidth(buttonStyle.getWidth().add(menuStyle.getWidth()));
    }
    
    // start rendering
    writer.startElement(HtmlConstants.SPAN, command);
    String itemClass = "tobago-toolBar-item";
    if (selected) {
      itemClass += " tobago-toolBar-item-selected";
    }
    if (disabled) {
      itemClass += " tobago-toolBar-item-disabled";
    }
    writer.writeClassAttribute(itemClass);
    HtmlRendererUtils.renderTip(command, writer);
    writer.writeStyleAttribute(itemStyle);

    writer.startElement(HtmlConstants.SPAN, command);
    writer.writeClassAttribute(
        selected ? "tobago-toolBar-button tobago-toolBar-button-selected" : "tobago-toolBar-button");
    writer.writeStyleAttribute(buttonStyle);
    writer.writeAttribute(HtmlAttributes.ONCLICK, commandClick != null ? commandClick : menuClick, true);
    // render icon
    if (showIcon && iconName != null) {
      writer.startElement(HtmlConstants.IMG, command);
      writer.writeAttribute(HtmlAttributes.SRC, image, false);
      String imageHover
          = ResourceManagerUtils.getImageWithPath(facesContext, HtmlRendererUtils.createSrc(iconName, "Hover"), true);
      if (imageHover != null) {
        writer.writeAttribute(HtmlAttributes.SRCDEFAULT, image, false);
        writer.writeAttribute(HtmlAttributes.SRCHOVER, imageHover, false);
      }
      writer.writeAttribute(HtmlAttributes.ALT, label.getText(), true);
      writer.writeStyleAttribute(iconStyle);
      writer.endElement(HtmlConstants.IMG);
    }
    // render label
    if (showLabel) {
      writer.startElement(HtmlConstants.SPAN, command);
      writer.writeClassAttribute("tobago-toolBar-label");
      writer.writeStyleAttribute(labelStyle);
      if (label.getText() != null) {
        HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
      }
      writer.endElement(HtmlConstants.SPAN);
    }

    if (separateButtons) {
      writer.endElement(HtmlConstants.SPAN);

      writer.startElement(HtmlConstants.SPAN, command);
      writer.writeClassAttribute("tobago-toolBar-menu");
      writer.writeStyleAttribute(menuStyle);
      writer.writeAttribute(HtmlAttributes.TYPE, "button", false);
      writer.writeAttribute(HtmlAttributes.ONCLICK, menuClick, true);
    }

    // render sub menu popup button
    if (popupMenu != null) {
      writer.startElement(HtmlConstants.IMG, command);
      String menuImage = ResourceManagerUtils.getImageWithPath(facesContext, "image/toolbarButtonMenu.gif");
      writer.writeAttribute(HtmlAttributes.SRC, menuImage, false);
      writer.writeStyleAttribute(openerStyle);
      writer.endElement(HtmlConstants.IMG);
      renderPopup(facesContext, writer, popupMenu);
    }
    writer.endElement(HtmlConstants.SPAN);
    writer.endElement(HtmlConstants.SPAN);

    return width.add(itemStyle.getWidth()).add(2); // XXX
    // computation of the width of the toolBar will not be used in the moment.
  }

  private Measure renderSeparator(
      FacesContext facesContext, UIToolBar toolBar, UIToolBarSeparator separator, TobagoResponseWriter writer,
      Measure width)
      throws IOException {
    if (!separator.isRendered()) {
      return width;
    }

    writer.startElement(HtmlConstants.SPAN, separator);
    writer.writeClassAttribute("tobago-toolBar-item tobago-toolBar-item-disabled");
    Style itemStyle = new Style();
    itemStyle.setHeight(getItemHeight(facesContext, toolBar));
    itemStyle.setWidth(Measure.valueOf(10));
    writer.writeStyleAttribute(itemStyle);

    writer.startElement(HtmlConstants.SPAN, separator);
    writer.writeClassAttribute("tobago-toolBar-separator");
    writer.endElement(HtmlConstants.SPAN);

    writer.endElement(HtmlConstants.SPAN);

    return width.add(itemStyle.getWidth()).add(2); // XXX
    // computation of the width of the toolBar will not be used in the moment.
  }

  protected Measure getItemHeight(FacesContext facesContext, Configurable toolBar) {
    final String iconSize = getIconSize((UIComponent) toolBar);
    final String labelPosition = getLabelPosition((UIComponent) toolBar);

    final boolean showIcon = !UIToolBar.ICON_OFF.equals(iconSize);
    final boolean iconBig = UIToolBar.ICON_BIG.equals(iconSize);
    final boolean iconSmall = UIToolBar.ICON_SMALL.equals(iconSize);
    final boolean showLabelBottom = UIToolBar.LABEL_BOTTOM.equals(labelPosition);
    final boolean showLabelRight = UIToolBar.LABEL_RIGHT.equals(labelPosition);
    final boolean showLabel = showLabelBottom || showLabelRight;

    final ResourceManager resources = getResourceManager();

    final Measure paddingTop = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-top");
    final Measure paddingMiddle = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-middle");
    final Measure paddingBottom = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-bottom");
    final Measure iconHeight = iconBig
        ? resources.getThemeMeasure(facesContext, toolBar, "custom.icon-big-height")
        : resources.getThemeMeasure(facesContext, toolBar, "custom.icon-small-height");
    final Measure labelHeight = resources.getThemeMeasure(facesContext, toolBar, "custom.label-height");

    Measure result = paddingTop;
    if (showIcon) {
      result = result.add(iconHeight);
      if (showLabel && showLabelBottom) {
        result = result.add(paddingMiddle);
        result = result.add(labelHeight);
      }
    } else {
      if (showLabel) {
        result = result.add(labelHeight);
      } else {
        // both off: use some reasonable defaults
        result = result.add(16);
      }
    }
    result = result.add(paddingBottom);
    return result;
  }

  private String createCommandOnClick(FacesContext facesContext, UICommandBase command) {
    if (hasNoCommand(command) && command.getFacet(Facets.MENUPOPUP) != null) {
      return null;
    } else {
      CommandRendererHelper helper = new CommandRendererHelper(facesContext, command);
      return helper.getOnclick();
    }
  }

  private boolean hasAnyCommand(UICommandBase command) {
    return !hasNoCommand(command);
  }

  private boolean hasNoCommand(UICommandBase command) {
    return command.getAction() == null
        && command.getActionListener() == null
        && command.getActionListeners().length == 0
        && command.getLink() == null
        && command.getAttributes().get(Attributes.ONCLICK) == null;
  }

  private String createMenuOnClick(UICommandBase command) {
    if (command.getFacet(Facets.MENUPOPUP) != null) {
      return "jQuery(this).find('a').click();event.stopPropagation();";
    } else {
      return null;
    }
  }

  private String getImage(
      FacesContext facesContext, String name, String iconSize, boolean disabled, boolean selected) {
    int pos = name.lastIndexOf('.');
    if (pos == -1) {
      pos = name.length(); // avoid exception if no '.' in name
    }
    String key = name.substring(0, pos);
    String ext = name.substring(pos);

    String size = "";
    if (UIToolBar.ICON_SMALL.equals(iconSize)) {
      size = "16";
    } else if (UIToolBar.ICON_BIG.equals(iconSize)) {
      size = "32";
    }
    String image = null;
    ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    if (disabled && selected) {
      image = resourceManager.getImage(facesContext, key + "SelectedDisabled" + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(facesContext, key + "SelectedDisabled" + ext, true);
      }
    }
    if (image == null && disabled) {
      image = resourceManager.getImage(facesContext, key + "Disabled" + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(facesContext, key + "Disabled" + ext, true);
      }
    }
    if (image == null && selected) {
      image = resourceManager.getImage(facesContext, key + "Selected" + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(facesContext, key + "Selected" + ext, true);
      }
    }
    if (image == null) {
      image = resourceManager.getImage(facesContext, key + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(facesContext, key + ext, true);
      }
    }

    return facesContext.getExternalContext().getRequestContextPath() + image;
  }

  private void renderPopup(FacesContext facesContext, TobagoResponseWriter writer, UIComponent popupMenu)
      throws IOException {
    writer.startElement(HtmlConstants.OL, popupMenu);
    writer.writeClassAttribute("tobago-menuBar");
      // TODO: use a different style class
    writer.writeStyleAttribute("display:inline;width:0;height:0;position:absolute;visibility:hidden;");
    RenderUtils.encode(facesContext, popupMenu);
    writer.endElement(HtmlConstants.OL);
  }

  @Override
  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }
}
