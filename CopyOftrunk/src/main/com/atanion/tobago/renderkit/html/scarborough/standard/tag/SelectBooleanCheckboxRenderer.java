/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends RendererBase {

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UIInput uiInput = (UIInput) component;

    String newValue = ((ServletRequest)facesContext.getExternalContext().getRequest())
        .getParameter(uiInput.getClientId(facesContext));
//    LogUtils.logParameter(((ServletRequest)facesContext.getExternalContext().getRequest()));
//    LOG.debug("decode: key='" + uiInput.getClientId(facesContext)
//        + "' value='" + newValue + "'  valid = " + uiInput.isValid());
    if (newValue != null) {
      uiInput.setValue(new Boolean(newValue));
    } else {
      uiInput.setValue(Boolean.FALSE);
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectBoolean component = (UISelectBoolean) uiComponent;

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    UIComponent label = ComponentUtil.provideLabel(facesContext, component);

    boolean inline = ComponentUtil.getBooleanAttribute(component, ATTR_INLINE);

    if (label != null && ! inline) {

      writer.startElement("table", component);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.writeAttribute("title", null, ATTR_TIP);

      writer.startElement("tr", null);
      writer.startElement("td", null);
    }

    Boolean currentValue = (Boolean)component.getValue();
    boolean checked = currentValue != null ? currentValue.booleanValue() : false;

    writer.startElement("input", component);
    writer.writeAttribute("type", "checkbox", null);
    writer.writeAttribute("value", "true", null);
    writer.writeAttribute("checked", checked);
    writer.writeNameAttribute(component.getClientId(facesContext));
    writer.writeComponentClass();
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeAttribute("title", null, ATTR_TIP);
    writer.endElement("input");

    if (label != null && ! inline) {
      writer.endElement("td");
      writer.startElement("td", null);
      writer.writeText("", null);
    }

    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    if (label != null && ! inline) {
      writer.endElement("td");
      writer.endElement("tr");
      writer.endElement("table");
    }


  }

// ///////////////////////////////////////////// bean getter + setter

}

