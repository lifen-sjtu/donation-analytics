package com.insights.data.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class QuickSelectUtil {

  public static <T> T quickselect(List<T> elements, Comparator<T> comparator, int k) {
    return quickselect(elements, comparator, 0, elements.size() - 1, k-1);

  }

  private static <T> T quickselect(List<T> elements, Comparator<T> comparator, int first, int last, int k) {
    if (first <= last) {
      int pivot = partition(elements, comparator, first, last);
      if (pivot == k) {
        return elements.get(k);
      }
      if (pivot > k) {
        return quickselect(elements, comparator, first, pivot - 1, k);
      }
      return quickselect(elements, comparator, pivot + 1, last, k);
    }
    return null;
  }

  private static <T> int partition(List<T> elements, Comparator<T> comparator, int first, int last) {
    int pivot = first + new Random().nextInt(last - first + 1);
    swap(elements, last, pivot);
    for (int i = first; i < last; i++) {
      if (comparator.compare(elements.get(i), elements.get(last)) < 0) {
        swap(elements, i, first);
        first++;
      }
    }
    swap(elements, first, last);
    return first;
  }

  private static <T> void swap(List<T> elements, int x, int y) {
    T temp = elements.get(x);
    elements.set(x, elements.get(y));
    elements.set(y, temp);
  }
}
