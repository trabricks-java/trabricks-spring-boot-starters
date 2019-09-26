package com.trabricks.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HtmlUtils {

  public String selected(Object data1, Object data2) {
    return this.htmlattr(data1, data2, "selected=\"selected\"", false);
  }

  public String checked(Object data1, Object data2) {
    return this.htmlattr(data1, data2, "checked=\"checked\"", false);
  }

  public String selected(Object data1, Object data2, boolean def) {
    return this.htmlattr(data1, data2, "selected=\"selected\"", def);
  }

  public String checked(Object data1, Object data2, boolean def) {
    return this.htmlattr(data1, data2, "checked=\"checked\"", def);
  }

  public String htmlattr(Object data1, Object data2, String attr, boolean def) {
    if (data1 == null || data2 == null) {
      return (def) ? attr : "";
    }

    String str1 = data1.toString().trim();

    if (data2 instanceof Collection) {
      List<String> arr = new ArrayList<String>();
      for (Object o : (Collection) data2) {
        arr.add(o.toString().trim().toLowerCase());
      }
      return arr.contains(str1.toLowerCase()) ? attr : "";
    } else {
      String str2 = data2.toString().trim();
      if (str1.isEmpty() || str2.isEmpty()) {
        return (def) ? attr : "";
      }

      boolean isnull = false;
      boolean contains = (str2.subSequence(0, 1).equals("%"));
			if (contains) {
				str2 = str2.substring(1);
			}

      if ((contains ? str1.contains(str2) : str1.equals(str2)) || (isnull && def)) {
        return attr;
      } else {
        return "";
      }
    }
  }


}
