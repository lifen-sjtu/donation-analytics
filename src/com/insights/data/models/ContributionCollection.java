package com.insights.data.models;

import com.insights.data.utils.QuickSelectUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * a dedicated data structure to optimize the percentile calculations.
 */
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

    /**
     * get the total amount with the necessary precision
     * @return if there is no decimal return a int-like string. if there is one decimal, return with one. same for two.
     */
    public String getTotalAmount() {
        if (Math.round(totalAmount) == totalAmount) {
            return String.format("%.0f", totalAmount);
        }
        if (Math.round(totalAmount * 10) == totalAmount * 10) {
            return String.format("%.1f", totalAmount);
        } else {
            return String.format("%.2f", totalAmount);
        }
    }

    /**
     * Use quick select to get Kth smallest value from a list. the average complexity is O(n).
     * @param percentile the required percentile
     * @return rounding value of the result
     */
    public String getAmountAtPercentile(int percentile) {
        int totalCount = contributions.size();
        int ordinalRank = (totalCount * percentile + 99) / 100;
        Contribution kthContribution = QuickSelectUtil.quickselect(
                contributions, Comparator.comparing(Contribution::getTransactionAmount), ordinalRank);
        return String.format("%.0f", kthContribution.getTransactionAmount());
    }

    public void insertNewContribution(Contribution contribution) {
        contributions.add(contribution);
        totalAmount += contribution.getTransactionAmount();
    }
}
