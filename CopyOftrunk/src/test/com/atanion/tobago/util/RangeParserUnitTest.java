/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 17.03.2004 11:16:26.
 * $Id$
 */
package com.atanion.tobago.util;

import junit.framework.TestCase;

public class RangeParserUnitTest extends TestCase {

  public void test() {

    int[] ints =  {0,5,10};
    String s = "0,5,10";
    checkEquals(ints, RangeParser.getIndices(s));
    s = "0, 5, 10";
    checkEquals(ints, RangeParser.getIndices(s));
    s = " 0 , 5 , 10 ";
    checkEquals(ints, RangeParser.getIndices(s));

    ints = new int[] {3,4,5,6,7,15,16,17};
    s = "3-7,15-17";
    checkEquals(ints, RangeParser.getIndices(s));
    s = "3-5,6,7,15,16-17";
    checkEquals(ints, RangeParser.getIndices(s));
    s = "3-5, 6, 7, 15, 16 - 17 ";
    checkEquals(ints, RangeParser.getIndices(s));

    ints = new int[] {3,4,5,6,7,15,14,13};
    s = "3-7,15-13";
    checkEquals(ints, RangeParser.getIndices(s));
    s = "3 - 7, 15 - 13";
    checkEquals(ints, RangeParser.getIndices(s));


  }

  private void checkEquals(int[] ints, int[] indices) {
    assertTrue(ints.length == indices.length);
    for (int i = 0; i < ints.length; i++) {
      assertTrue(ints[i] == indices[i]);
    }
  }
}
