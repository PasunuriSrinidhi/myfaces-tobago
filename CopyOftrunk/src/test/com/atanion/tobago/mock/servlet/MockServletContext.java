/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 25.08.2004 10:54:04.
 * $Id: MockServletContext.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package com.atanion.tobago.mock.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.Servlet;
import java.util.Enumeration;
import java.util.Set;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;

public class MockServletContext implements ServletContext {

  public Set getResourcePaths(String reference) {
    return null;
  }

  public Object getAttribute(String s) {
    return null;
  }

  public Enumeration getAttributeNames() {
    return null;
  }

  public ServletContext getContext(String s) {
    return null;
  }

  public String getInitParameter(String s) {
    return null;
  }

  public Enumeration getInitParameterNames() {
    return null;
  }

  public int getMajorVersion() {
    return 0;
  }

  public String getMimeType(String s) {
    return null;
  }

  public int getMinorVersion() {
    return 0;
  }

  public RequestDispatcher getNamedDispatcher(String s) {
    return null;
  }

  public String getRealPath(String s) {
    return null;
  }

  public RequestDispatcher getRequestDispatcher(String s) {
    return null;
  }

  public URL getResource(String s) throws MalformedURLException {
    return null;
  }

  public InputStream getResourceAsStream(String s) {
    return null;
  }

  public Set getResourcePaths() {
    return null;
  }

  public String getServerInfo() {
    return null;
  }

  public Servlet getServlet(String s) throws ServletException {
    return null;
  }

  public String getServletContextName() {
    return null;
  }

  public Enumeration getServletNames() {
    return null;
  }

  public Enumeration getServlets() {
    return null;
  }

  public void log(Exception e, String s) {
  }

  public void log(String s) {
  }

  public void log(String s, Throwable throwable) {
  }

  public void removeAttribute(String s) {
  }

  public void setAttribute(String s, Object o) {
  }
}