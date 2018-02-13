package com.insights.data;

import com.insights.data.models.TestContributionCollection;
import com.insights.data.processors.TestDonorProcessor;
import com.insights.data.processors.TestRecipientProcessor;
import com.insights.data.utils.TestQuickSelectUtil;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestRunner {
  public static void main(String[] args) {

    Result result = JUnitCore.runClasses(TestQuickSelectUtil.class, TestContributionCollection.class,
        TestDonorProcessor.class, TestRecipientProcessor.class);

    for (Failure failure : result.getFailures()) {
      System.out.println(failure.getTrace());
    }

    System.out.println(result.wasSuccessful());
  }
}