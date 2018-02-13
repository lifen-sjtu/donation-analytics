package com.insights.data.models;

import java.util.*;

/**
 * This model is the top level aggregation of all contributions. The key of the recipient is the id, but more importantly,
 * all contributions are dumped into this model based on the recipient of the contribution.
 * Inside the model contributions are split into two groups, those from repeat donors and non repeat donors.
 */
public class Recipient {
    private String id;

    // For contributions from repeat donor, the information we need is the list of contributions in a particular year
    // and zipcode. so the key to the map is zipcode|year. the value is a collection wrapper which is responsible for
    // the computations required in the problem such as total and percentile.
    private Map<String, ContributionCollection> zipCodeYearToWrapperMapForRepeatDonor;

    // For contributions from non-repeat donor, we need an easy way to convert them into repeat map once a certain donor
    // is detected as repeat donor. so the key to the map is donor, and value is a collection of contributions from this
    // donor
    private Map<Donor, List<Contribution>> nonRepeatDonorContributionsMap;

    public Recipient(String id) {
        this.id = id;
        this.nonRepeatDonorContributionsMap = new HashMap<>();
        this.zipCodeYearToWrapperMapForRepeatDonor = new HashMap<>();
    }

    /**
     * the process of converting contributions from a non-repeat donor in nonRepeatDonorContributionsMap to
     * the corresponding entry in zipCodeYearToWrapperMapForRepeatDonor.
     * @param donor the donor we just identified as repeat and would like to convert
     */
    public void markDonorAsRepeat(Donor donor) {
        List<Contribution> donorContributions = nonRepeatDonorContributionsMap.get(donor);
        for (Contribution contribution : donorContributions) {
            insertContributionForRepeatDonor(contribution);
        }
        nonRepeatDonorContributionsMap.remove(donor);
    }

    /**
     * insert a contribution to repeat donor group
     * @param contribution the contribution we are going to insert
     */
    public void insertContributionForRepeatDonor(Contribution contribution) {
        String key = contribution.getDonor().getZipCode()+"|"+contribution.getTransactionDate().getYear();
        if (zipCodeYearToWrapperMapForRepeatDonor.containsKey(key)) {
            zipCodeYearToWrapperMapForRepeatDonor.get(key).insertNewContribution(contribution);
        } else {
            ContributionCollection contributionCollection = new ContributionCollection();
            contributionCollection.insertNewContribution(contribution);
            zipCodeYearToWrapperMapForRepeatDonor.put(key, contributionCollection);
        }
    }

    /**
     * insert a contribution to non-repeat donor group
     * @param contribution the contribution we are going to insert
     */
    public void insertContributionForNonRepeatDonor(Contribution contribution) {
        Donor donor = contribution.getDonor();
        if (nonRepeatDonorContributionsMap.containsKey(donor)) {
            nonRepeatDonorContributionsMap.get(donor).add(contribution);
        } else {
            List<Contribution> contributions = new ArrayList<>();
            contributions.add(contribution);
            nonRepeatDonorContributionsMap.put(donor, contributions);
        }
    }

    /**
     * get contribution statistics from all contributions in given zip code and year
     * @param percentile the percentile data in the request
     * @param zipCode the zipcode of the contribution
     * @param year the year of the contribution
     * @return the result in desired format, joined by |
     */
    public String getContributionStatsFromRepeatDonor(int percentile, String zipCode, int year) {
        String key = zipCode+"|"+year;
        ContributionCollection contributionCollection = zipCodeYearToWrapperMapForRepeatDonor.get(key);
        if (contributionCollection == null) {
            return null;
        } else {
            return this.id+"|"+key+"|"+ contributionCollection.getAmountAtPercentile(percentile)+
                "|"+ contributionCollection.getTotalAmount()+"|"+ contributionCollection.getTotalCount();
        }

    }

}
