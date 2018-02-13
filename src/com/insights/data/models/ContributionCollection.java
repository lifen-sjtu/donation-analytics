package com.insights.data.models;

import com.insights.data.utils.QuickSelectUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ContributionCollection {
  private float totalAmount;
  private List<Contribution> contributions;

  public ContributionCollection() {
    this.totalAmount = 0;
    this.contributions = new ArrayList<>();
  }

  public int getTotalCount() {
    return contributions.size();
  }

  public String getTotalAmount() {
    if (Math.round(totalAmount) == totalAmount) {
      return String.format("%.0f", totalAmount);
    } if (Math.round(totalAmount * 10) == totalAmount * 10) {
      return String.format("%.1f", totalAmount);
    } else {
      return String.format("%.2f", totalAmount);
    }

  }

  public String getAmountAtPercentile(int percentile) {
    int totalCount = contributions.size();
    int ordinalRank = (totalCount * percentile + 99)/ 100;
    Contribution kthContribution = QuickSelectUtil.quickselect(
        contributions, Comparator.comparing(Contribution::getTransactionAmount), ordinalRank);
    return String.format("%.0f", kthContribution.getTransactionAmount());
  }

  public void insertNewContribution(Contribution contribution) {
    contributions.add(contribution);
    totalAmount += contribution.getTransactionAmount();
  }
}
