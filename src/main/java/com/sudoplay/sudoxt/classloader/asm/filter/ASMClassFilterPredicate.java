package com.sudoplay.sudoxt.classloader.asm.filter;

import com.sudoplay.sudoxt.classloader.filter.ClassFilterPredicate;
import com.sudoplay.sudoxt.classloader.filter.IClassFilter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class ASMClassFilterPredicate extends
    ClassFilterPredicate {

  public ASMClassFilterPredicate(IClassFilter[] classFilters) {
    super(classFilters);
  }

  @Override
  public boolean isAllowed(String name) {
    name = stripClassName(name);
    name = name.replaceAll("/", ".");
    return super.isAllowed(name);
  }

  @NotNull
  /* package */ String stripClassName(String name) {
    char[] chars = name.toCharArray();
    int index;

    for (index = 0; index < chars.length; index++) {

      if (chars[index] == '[') {
        continue;
      }
      break;
    }

    name = name.substring(index);

    if (name.startsWith("L") && name.endsWith(";")) {
      name = name.substring(1, name.length() - 1);

    } else {
      name = name.substring(index);
    }

    return name;
  }
}
