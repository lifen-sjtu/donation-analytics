package com.insights.data.processors;

import com.insights.data.models.Donor;
import com.insights.data.models.DonorStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestDonorProcessor {
  public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");

  @Test
  public void testHappyPath() {
    DonorProcessor donorProcessor = new DonorProcessor();
    Donor donor1 = new Donor("A", "94085");
    Donor donor2 = new Donor("B", "94085");

    LocalDate localDate1 = LocalDate.parse("02012017", DATE_FORMATTER);
    LocalDate localDate2 = LocalDate.parse("01012018", DATE_FORMATTER);
    LocalDate localDate3 = LocalDate.parse("02082018", DATE_FORMATTER);
    LocalDate localDate4 = LocalDate.parse("02072018", DATE_FORMATTER);

    DonorStatus donorStatus1 = donorProcessor.getDonorStatusAndUpdateAfterwards(donor1, localDate1);
    assertEquals(donorStatus1, DonorStatus.NON_REPEAT);

    DonorStatus donorStatus2 = donorProcessor.getDonorStatusAndUpdateAfterwards(donor2, localDate2);
    assertEquals(donorStatus2, DonorStatus.NON_REPEAT);

    DonorStatus donorStatus3 = donorProcessor.getDonorStatusAndUpdateAfterwards(donor1, localDate3);
    assertEquals(donorStatus3, DonorStatus.NON_REPEAT_TO_REPEAT);

    DonorStatus donorStatus4 = donorProcessor.getDonorStatusAndUpdateAfterwards(donor1, localDate4);
    assertEquals(donorStatus4, DonorStatus.REPEAT);
  }

  @Test
  public void testContributionsOnSameDate() {
    DonorProcessor donorProcessor = new DonorProcessor();
    Donor donor1 = new Donor("A", "94085");

    LocalDate localDate1 = LocalDate.parse("02012018", DATE_FORMATTER);

    DonorStatus donorStatus1 = donorProcessor.getDonorStatusAndUpdateAfterwards(donor1, localDate1);
    assertEquals(donorStatus1, DonorStatus.NON_REPEAT);

    DonorStatus donorStatus2 = donorProcessor.getDonorStatusAndUpdateAfterwards(donor1, localDate1);
    assertEquals(donorStatus2, DonorStatus.NON_REPEAT);

    DonorStatus donorStatus3 = donorProcessor.getDonorStatusAndUpdateAfterwards(donor1, localDate1);
    assertEquals(donorStatus3, DonorStatus.NON_REPEAT);
  }

  @Test
  public void testSecondContributionOnEarlierDate() {
    DonorProcessor donorProcessor = new DonorProcessor();
    Donor donor1 = new Donor("A", "94085");

    LocalDate localDate1 = LocalDate.parse("02012018", DATE_FORMATTER);
    LocalDate localDate2 = LocalDate.parse("01012017", DATE_FORMATTER);

    DonorStatus donorStatus1 = donorProcessor.getDonorStatusAndUpdateAfterwards(donor1, localDate1);
    assertEquals(donorStatus1, DonorStatus.NON_REPEAT);

    DonorStatus donorStatus2 = donorProcessor.getDonorStatusAndUpdateAfterwards(donor1, localDate2);
    assertEquals(donorStatus2, DonorStatus.NON_REPEAT);

  }


}
