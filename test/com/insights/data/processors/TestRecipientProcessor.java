package com.insights.data.processors;

import com.insights.data.models.Donor;
import com.insights.data.models.Recipient;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRecipientProcessor {


  @Test
  public void testFindRecipientsByDonor() {
    Donor donor1 = new Donor("A", "94085");
    Donor donor2 = new Donor("B", "94087");
    Recipient recipient1 = new Recipient("10");
    Recipient recipient2 = new Recipient("20");

    RecipientProcessor processor = new RecipientProcessor();
    processor.insertRecipientToDonorMap(donor1, recipient1);
    assertEquals(processor.findRecipientsByDonor(donor1).size(), 1);
    assertEquals(processor.findRecipientsByDonor(donor2).size(), 0);

    processor.insertRecipientToDonorMap(donor1, recipient2);
    assertEquals(processor.findRecipientsByDonor(donor1).size(), 2);
    assertEquals(processor.findRecipientsByDonor(donor2).size(), 0);

  }
}
