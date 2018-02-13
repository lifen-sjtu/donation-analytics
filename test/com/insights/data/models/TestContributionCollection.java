package com.insights.data.models;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class TestContributionCollection {

  @Test
  public void testHappyPath() {
    int percentile = 50;
    Contribution contribution1 = new Contribution(200.0f);
    Contribution contribution2 = new Contribution(100.0f);
    Contribution contribution3 = new Contribution(400.0f);
    Contribution contribution4 = new Contribution(300.0f);

    ContributionCollection contributionCollection = new ContributionCollection();
    contributionCollection.insertNewContribution(contribution1);
    assertEquals(contributionCollection.getTotalAmount(), "200");
    assertEquals(contributionCollection.getTotalCount(), 1);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "200");

    contributionCollection.insertNewContribution(contribution2);
    assertEquals(contributionCollection.getTotalAmount(), "300");
    assertEquals(contributionCollection.getTotalCount(), 2);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "100");

    contributionCollection.insertNewContribution(contribution3);
    assertEquals(contributionCollection.getTotalAmount(), "700");
    assertEquals(contributionCollection.getTotalCount(), 3);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "200");

    contributionCollection.insertNewContribution(contribution4);
    assertEquals(contributionCollection.getTotalAmount(), "1000");
    assertEquals(contributionCollection.getTotalCount(), 4);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "200");

  }

  @Test
  public void testHundredPercentile() {
    int percentile = 100;
    Contribution contribution1 = new Contribution(200.0f);
    Contribution contribution2 = new Contribution(100.0f);
    Contribution contribution3 = new Contribution(400.0f);

    ContributionCollection contributionCollection = new ContributionCollection();
    contributionCollection.insertNewContribution(contribution1);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "200");

    contributionCollection.insertNewContribution(contribution2);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "200");

    contributionCollection.insertNewContribution(contribution3);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "400");

  }

  @Test
  public void testOnePercentile() {
    int percentile = 1;
    Contribution contribution1 = new Contribution(200.0f);
    Contribution contribution2 = new Contribution(100.0f);
    Contribution contribution3 = new Contribution(400.0f);

    ContributionCollection contributionCollection = new ContributionCollection();
    contributionCollection.insertNewContribution(contribution1);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "200");

    contributionCollection.insertNewContribution(contribution2);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "100");

    contributionCollection.insertNewContribution(contribution3);
    assertEquals(contributionCollection.getAmountAtPercentile(percentile), "100");

  }


}
