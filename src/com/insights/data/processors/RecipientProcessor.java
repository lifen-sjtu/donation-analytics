package com.insights.data.processors;

import com.insights.data.models.Contribution;
import com.insights.data.models.Donor;
import com.insights.data.models.Recipient;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * This class holds the states related to recipients so it could support multiple queries against recipients in memory.
 */
public class RecipientProcessor {
    private Map<String, Recipient> recipientMap;
    private Map<Donor, Set<Recipient>> donorToRecipientsMap;

    public RecipientProcessor() {
        this.recipientMap = new HashMap<>();
        this.donorToRecipientsMap = new HashMap<>();
    }

    /**
     * fetch the recipient entity from the memory. If there is no entry, create a new one.
     * @param contribution the contribution from input which will contain the recipient id.
     * @return the recipient entity.
     */
    public Recipient getRecipientByContribution(Contribution contribution) {
        Recipient recipient;
        if (recipientMap.containsKey(contribution.getRecipient())) {
            recipient = recipientMap.get(contribution.getRecipient());
        } else {
            recipient = new Recipient(contribution.getRecipient());
            recipientMap.put(contribution.getRecipient(), recipient);
        }
        return recipient;
    }

    /**
     * insert the recipient to the corresponding donor to recipient set map
     * @param donor the donor who contributes to the recipient
     * @param recipient the recipient of the contribution
     */
    public void insertRecipientToDonorMap(Donor donor, Recipient recipient) {
        if (donorToRecipientsMap.containsKey(donor)) {
            donorToRecipientsMap.get(donor).add(recipient);
        } else {
            Set<Recipient> recipients = new HashSet<>();
            recipients.add(recipient);
            donorToRecipientsMap.put(donor, recipients);
        }
    }


    /**
     * find the set of recipients by a given donor. this is useful because when we identify a donor to be a repeat donor,
     * we want to update its contributions everywhere. currently contributions are organized by recipients.
     * @param donor the donor we want to query.
     * @return the set of recipients.
     */
    public Set<Recipient> findRecipientsByDonor(Donor donor) {
        if (donorToRecipientsMap.containsKey(donor)) {
            return donorToRecipientsMap.get(donor);
        } else {
            return Collections.emptySet();
        }
    }

}
