package com.insights.data.processors;

import com.insights.data.models.Contribution;
import com.insights.data.models.Donor;
import com.insights.data.models.DonorStatus;
import com.insights.data.models.Recipient;

import java.util.*;

/**
 * Contribution processor handles the main logic of reading a contribution from stream and return the expected output
 * to print. It mainly delegates to {@link DonorProcessor} and {@link RecipientProcessor} to handle domain specific logic.
 */
public class ContributionProcessor {

    private int percentile;
    private DonorProcessor donorProcessor;
    private RecipientProcessor recipientProcessor;

    public ContributionProcessor(int percentile) {
        this.percentile = percentile;
        this.donorProcessor = new DonorProcessor();
        this.recipientProcessor = new RecipientProcessor();
    }


    /**
     * process a contribution from input stream
     * @param contribution the input from main class
     * @return the expected string if this is from a valid repeat donor, null otherwise.
     */
    public String processNewContribution(Contribution contribution) {
        Recipient recipient = recipientProcessor.getRecipientByContribution(contribution);
        Donor donor = contribution.getDonor();
        recipientProcessor.insertRecipientToDonorMap(donor, recipient);

        DonorStatus status = donorProcessor.getDonorStatusAndUpdateAfterwards(donor, contribution.getTransactionDate());

        if (status == DonorStatus.NON_REPEAT) {
            // non repeat donor, only need to add the data to recipient's data store
            recipient.insertContributionForNonRepeatDonor(contribution);
            return null;
        } else if (status == DonorStatus.NON_REPEAT_TO_REPEAT) {
            recipient.insertContributionForNonRepeatDonor(contribution);
            // update all recipients that receives the contribution from this donor
            Set<Recipient> impactedRecipients = recipientProcessor.findRecipientsByDonor(donor);
            for (Recipient impactedRecipient: impactedRecipients) {
                impactedRecipient.markDonorAsRepeat(donor);
            }
            return recipient.getContributionStatsFromRepeatDonor(
                percentile, contribution.getDonor().getZipCode(), contribution.getTransactionDate().getYear());
        } else {
            // donor status was already marked as REPEAT
            recipient.insertContributionForRepeatDonor(contribution);
            return recipient.getContributionStatsFromRepeatDonor(
                percentile, contribution.getDonor().getZipCode(), contribution.getTransactionDate().getYear());
        }
    }
}
