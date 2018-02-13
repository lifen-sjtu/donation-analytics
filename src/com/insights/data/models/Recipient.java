package com.insights.data.models;

import java.util.*;

/**
 * Created by Fen Li on 2/11/18.
 */
public class Recipient {
    private String id;
    // For contributions from repeat donor, the information we need is the list of contributions in a particular year
    // and zipcode. so maintain a map whose key is zipcode|year to speed up the query
    private Map<String, ContributionCollection> zipCodeYearToWrapperMapForRepeatDonor;

    // For contributions from non-repeat donor, we need an easy way to convert them into repeat map once a certain donor
    // is detected as repeat donor.
    private Map<Donor, List<Contribution>> nonRepeatDonorContributionsMap;

    public Recipient(String id) {
        this.id = id;
        this.nonRepeatDonorContributionsMap = new HashMap<>();
        this.zipCodeYearToWrapperMapForRepeatDonor = new HashMap<>();
    }

    public void markDonorAsRepeat(Donor donor) {
        List<Contribution> donorContributions = nonRepeatDonorContributionsMap.get(donor);
        for (Contribution contribution : donorContributions) {
            insertContributionForRepeatDonor(contribution);
        }
        nonRepeatDonorContributionsMap.remove(donor);
    }

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
