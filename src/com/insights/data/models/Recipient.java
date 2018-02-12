package com.insights.data.models;

import java.util.*;

/**
 * Created by Fen Li on 2/11/18.
 */
public class Recipient {
    private String id;
    // new to handle the case when caller calls markDonorAsRepeat first, then call insertContributionForRepeatDonor
    private Map<String, List<Contribution>> repeatDonorContributionsMap;
    private Map<Donor, List<Contribution>> nonRepeatDonorContributionsMap;

    public Recipient(String id) {
        this.id = id;
        repeatDonorContributionsMap = new HashMap<>();
        nonRepeatDonorContributionsMap = new HashMap<>();
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
        if (repeatDonorContributionsMap.containsKey(key)) {
            repeatDonorContributionsMap.get(key).add(contribution);
        } else {
            List<Contribution> contributions = new ArrayList<>();
            contributions.add(contribution);
            repeatDonorContributionsMap.put(key, contributions);
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

    public String getAnswer(int percentile, String zipCode, int year) {
        String key = zipCode+"|"+year;
        List<Contribution> contributions = repeatDonorContributionsMap.get(key);
        if (contributions == null) {
            return "";
        } else {
            float totalAmount = 0;
            int totalCount = contributions.size();
            int ordinalRank = (totalCount * percentile + 99)/ 100;
            Collections.sort(contributions, Comparator.comparing(Contribution::getTransactionAmount));
            for (Contribution contribution : contributions) {
                totalAmount += contribution.getTransactionAmount();
            }
            return this.id+"|"+key+"|"+contributions.get(ordinalRank-1).getTransactionAmount()+"|"+totalAmount+"|"+contributions.size();
        }

    }

}
