package com.insights.data.processors;

import com.insights.data.models.Contribution;
import com.insights.data.models.Donor;
import com.insights.data.models.Recipient;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class RecipientProcessor {
  private Map<String, Recipient> recipientMap;
  private Map<Donor, Set<Recipient>> donorToRecipientsMap;

  public RecipientProcessor() {
    this.recipientMap = new HashMap<>();
    this.donorToRecipientsMap = new HashMap<>();
  }

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

  public void insertRecipientToDonorMap(Donor donor, Recipient recipient) {
    if (donorToRecipientsMap.containsKey(donor)) {
      donorToRecipientsMap.get(donor).add(recipient);
    } else {
      Set<Recipient> recipients = new HashSet<>();
      recipients.add(recipient);
      donorToRecipientsMap.put(donor, recipients);
    }
  }


  public Set<Recipient> findRecipientsByDonor(Donor donor) {
    if (donorToRecipientsMap.containsKey(donor)) {
      return donorToRecipientsMap.get(donor);
    } else {
      return Collections.emptySet();
    }
  }

}
