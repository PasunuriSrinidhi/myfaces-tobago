/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 06.09.2002 at 15:15:51.
 * $Id: TurnoverSelection.java 1269 2005-08-08 20:20:19 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package com.atanion.tobago.demo.model.banking;

import javax.faces.model.SelectItem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;

public class TurnoverSelection {

  public static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

// ///////////////////////////////////////////// attributes

  private String account;
  private List<SelectItem> accountItems;

  private String from;

  private String to;

// ///////////////////////////////////////////// constructor

  public TurnoverSelection() {
      Calendar today = new GregorianCalendar();
      to = format.format(today.getTime());
      today.set(
          today.get(Calendar.YEAR),
          today.get(Calendar.MONTH) - 1,
          today.get(Calendar.DAY_OF_MONTH));
      from = format.format(today.getTime());

    accountItems = new ArrayList<SelectItem>();
    accountItems.add(new SelectItem("1234567000"));
    accountItems.add(new SelectItem("1234567001"));
    accountItems.add(new SelectItem("1234567002"));
    accountItems.add(new SelectItem("1234567003"));
  }

// ///////////////////////////////////////////// bean getter + setter

  public boolean isValid() {
    try {
      format.parse(from);
      format.parse(to);
    } catch (ParseException e) {
      return false;
    }

    return true;
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public List<SelectItem> getAccountItems() {
    return accountItems;
  }

  public void setAccountItems(List<SelectItem> accountItems) {
    this.accountItems = accountItems;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }
}
