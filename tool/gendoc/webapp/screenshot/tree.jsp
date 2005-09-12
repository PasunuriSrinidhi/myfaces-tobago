<%@ page import="javax.swing.tree.DefaultMutableTreeNode" %>
<%@ page import="javax.swing.tree.MutableTreeNode" %>
<%@ page import="org.apache.myfaces.tobago.model.TreeState" %>
<%--
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
--%>

<%
  DefaultMutableTreeNode tree;
  TreeState treeState;

  tree = new DefaultMutableTreeNode("Category");
  tree.insert(new DefaultMutableTreeNode("Sports"), 0);
  tree.insert(new DefaultMutableTreeNode("Movies"), 0);
  DefaultMutableTreeNode music = new DefaultMutableTreeNode("Music");
  tree.insert(music, 0);
  tree.insert(new DefaultMutableTreeNode("Games"), 0);
  DefaultMutableTreeNode temp = new DefaultMutableTreeNode("Science");
  temp.insert(
      new DefaultMutableTreeNode("Geography"), 0);
  temp.insert(
      new DefaultMutableTreeNode("Mathematics"), 0);
  DefaultMutableTreeNode temp2 = new DefaultMutableTreeNode("Astronomy");
  temp2.insert(new DefaultMutableTreeNode("Education"), 0);
  temp2.insert(new DefaultMutableTreeNode("Pictures"), 0);
  temp.insert(temp2, 2);
  tree.insert(temp, 2);
  treeState = new TreeState();
  treeState.addExpandState(tree);
  treeState.addExpandState(temp);
  treeState.addSelection(temp2);
  treeState.setMarker(music);
request.setAttribute("tree", tree);
request.setAttribute("treeState", treeState);
%>

<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="tree">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="150px;1*" />
        </f:facet>
        <t:tree state="#{treeState}" value="#{tree}" id="screenshotTree"
            idReference="userObject"
            nameReference="userObject"
            showIcons="true"
            showJunctions="true"
            showRootJunction="true"
            showRoot="true"
            selectable="single"
            mutable="false"
            >
        </t:tree>


        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>

