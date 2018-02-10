package com.insights.data.processors;

import com.insights.data.models.Contribution;
import com.insights.data.models.Donor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Fen Li on 2/9/18.
 */
public class ContributionProcessor {

    private int percentile;
    private Map<String, List<Contribution>> recipientToContributionList;
    private Set<Donor> repeatDonors;
    private Map<Donor, Date> donorToDate;

    public ContributionProcessor(int percentile) {
        this.percentile = percentile;
        this.recipientToContributionList = new HashMap<>();
        this.repeatDonors = new HashSet<>();
        this.donorToDate = new HashMap<>();
    }

    public String insertNewContribution(Contribution contribution) {
        if (recipientToContributionList.containsKey(contribution.getRecipient())) {
            recipientToContributionList.get(contribution.getRecipient()).add(contribution);
        } else {
            List<Contribution> list = new ArrayList<>();
            list.add(contribution);
            recipientToContributionList.put(contribution.getRecipient(), list);
        }

        if (isRepeatDonorDuringThisContribution(contribution.getDonor(), contribution.getTransactionDate())) {

            List<Contribution> qualifiedContributions = getQualifiedContributions(
                    contribution.getRecipient(),
                    contribution.getTransactionDate().getYear(),
                    contribution.getDonor().getZipCode()
            );

            return computeResult(qualifiedContributions,
                    contribution.getRecipient(),
                    contribution.getTransactionDate().getYear(),
                    contribution.getDonor().getZipCode());

        } else {
            return "";
        }

    }

    // question: if we find one donor in 2018, then 2017. we know he is a repeat but don't need to do anything for now.
    // what if there is a third contribution in 2015?
    private boolean isRepeatDonorDuringThisContribution(Donor donor, Date date) {
        if (repeatDonors.contains(donor)) {
            return true;
        }
        // if this is the first time we find this donor as repeat one, update state
        if (donorToDate.containsKey(donor)) {
            repeatDonors.add(donor);
            return donorToDate.get(donor).before(date);
        } else {
            donorToDate.put(donor, date);
            return false;
        }

    }

    private List<Contribution> getQualifiedContributions(String recipient, int year, String zipCode) {
        if (recipientToContributionList.containsKey(recipient)) {
            return recipientToContributionList.get(recipient).stream()
                    .filter(contribution ->
                            repeatDonors.contains(contribution.getDonor()) &&
                            contribution.getTransactionDate().getYear() == year &&
                            contribution.getDonor().getZipCode().equals(zipCode))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private String computeResult(List<Contribution> contributions, String recipient, int year, String zipCode) {
        float totalAmount = 0;
        int totalCount = contributions.size();
        int ordinalRank = (totalCount * percentile + 99)/ 100;
        Collections.sort(contributions, Comparator.comparing(Contribution::getTransactionAmount));
        for (Contribution contribution : contributions) {
            totalAmount += contribution.getTransactionAmount();
        }
        return recipient+"|"+zipCode+"|"+year+"|"+contributions.get(ordinalRank-1).getTransactionAmount()+"|"+totalAmount+"|"+contributions.size();
    }
}
