package com.insights.data.processors;

import com.insights.data.models.Donor;
import com.insights.data.models.DonorStatus;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DonorProcessor {
  private Set<Donor> repeatDonors;
  private Map<Donor, Integer> donorToYear;

  public DonorProcessor() {
    this.repeatDonors = new HashSet<>();
    this.donorToYear = new HashMap<>();
  }

  public DonorStatus getDonorStatusAndUpdateAfterwards(Donor donor, LocalDate currentDate) {
    if (donorToYear.containsKey(donor)) {
      // only if we found a prior year in record we will mark this as repeat.
      // this means if the contribution is from the same year as previously recorded, it won't be marked.
      boolean currentDateIsLater = currentDate.getYear() > donorToYear.get(donor);
      if (currentDateIsLater) {
        if (repeatDonors.contains(donor)) {
          return DonorStatus.REPEAT;
        } else {
          // detect this donor as repeat for the first time
          // no need to update donorToYear map since the current entry is earlier
          repeatDonors.add(donor);
          return DonorStatus.NON_REPEAT_TO_REPEAT;
        }
      } else {
        // Found an earlier contribution. No need to mark as repeat.
        donorToYear.put(donor, currentDate.getYear());
        return DonorStatus.NON_REPEAT;
      }
    } else {
      donorToYear.put(donor, currentDate.getYear());
      return DonorStatus.NON_REPEAT;
    }
  }

}
