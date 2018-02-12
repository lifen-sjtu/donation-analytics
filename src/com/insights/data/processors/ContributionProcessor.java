package com.insights.data.processors;

import com.insights.data.models.Contribution;
import com.insights.data.models.Donor;
import com.insights.data.models.Recipient;

import java.util.*;

/**
 * Created by Fen Li on 2/9/18.
 */
public class ContributionProcessor {

    private int percentile;
    private Map<String, Recipient> recipientMap;
    private Set<Donor> repeatDonors;
    private Map<Donor, Date> donorToDate;
    private Map<Donor, Set<Recipient>> donorToRecipientsMap;

    public ContributionProcessor(int percentile) {
        this.percentile = percentile;
        this.recipientMap = new HashMap<>();
        this.repeatDonors = new HashSet<>();
        this.donorToDate = new HashMap<>();
        donorToRecipientsMap = new HashMap<>();
    }


    public String insertNewContribution(Contribution contribution) {
        Recipient recipient;
        if (recipientMap.containsKey(contribution.getRecipient())) {
            recipient = recipientMap.get(contribution.getRecipient());
        } else {
            recipient = new Recipient(contribution.getRecipient());
            recipientMap.put(contribution.getRecipient(), recipient);
        }

        Donor donor = contribution.getDonor();
        if (donorToRecipientsMap.containsKey(donor)) {
            donorToRecipientsMap.get(donor).add(recipient);
        } else {
            Set<Recipient> recipients = new HashSet<>();
            recipients.add(recipient);
            donorToRecipientsMap.put(donor, recipients);
        }

        int status = getDonorStatus(donor, contribution.getTransactionDate());

        if (status < 0) {
            // non repeat donor, only need to add the data to recipient's data store
            recipient.insertContributionForNonRepeatDonor(contribution);
            return "";
        } else if (status == 0) {
            recipient.insertContributionForNonRepeatDonor(contribution);
            // update all recipients that the donor is a repeat donor
            Set<Recipient> impactedRecipients = donorToRecipientsMap.get(donor);
            for (Recipient impactedRecipient: impactedRecipients) {
                impactedRecipient.markDonorAsRepeat(donor);
            }
            return recipient.getAnswer(percentile, contribution.getDonor().getZipCode(), contribution.getTransactionDate().getYear());
        } else {
            // donor is already a repeat
            recipient.insertContributionForRepeatDonor(contribution);
            return recipient.getAnswer(percentile, contribution.getDonor().getZipCode(), contribution.getTransactionDate().getYear());
        }
    }


    // update donor date if it is earlier so we won't miss any repeat
    private int getDonorStatus(Donor donor, Date date) {
        if (donorToDate.containsKey(donor)) {
            boolean newDateIsLater = date.after(donorToDate.get(donor));
            if (newDateIsLater) {
                if (repeatDonors.contains(donor)) {
                    return 1;
                } else {
                    // detect this donor as repeat for the first time
                    repeatDonors.add(donor);
                    return 0;
                }
            } else {
                // TODO: although the date I found is earlier than previous contributions, but it is already in
                // repeatDonors set. Should I return 1 or -1?
                donorToDate.put(donor, date);
                // TODO: do i need to insert donor to repeatDonors set in this case?
                return -1;
            }
        } else {
            donorToDate.put(donor, date);
            return -1;
        }
    }
}
