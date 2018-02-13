package com.insights.data.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestQuickSelectUtil {
  @Test
  public void testGetTopNHappyPath() {

    Comparator<Integer> comparator = new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
      }
    };
    List<Integer> elements = Arrays.asList(4, 3, 6, 10, 5);
    List<Integer> expected = Arrays.asList(3, 4, 5, 6, 10);
    for (int i = 0; i < elements.size(); i++) {
      assertEquals(QuickSelectUtil.quickselect(elements, comparator, i+1), expected.get(i));
    }

  }
}
