package com.sudoplay.sudoext.classloader.filter;

/**
 * Created by codetaylor on 3/1/2017.
 */
public class ClassFilterPredicate implements
    IClassFilterPredicate {

  private IClassFilter[] classFilters;

  public ClassFilterPredicate(IClassFilter[] classFilters) {
    this.classFilters = classFilters;
  }

  @Override
  public boolean isAllowed(String name) {

    boolean isAllowed = false;
    boolean isRestricted = false;

    for (IClassFilter classFilter : this.classFilters) {

      if (classFilter.isAllowed(name)) {
        isAllowed = true;
      }

      if (classFilter.isRestricted(name)) {
        isRestricted = true;
      }
    }

    // return true if is explicitly allowed and not explicitly restricted
    return isAllowed && !isRestricted;
  }
}
