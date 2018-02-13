package com.insights.data.processors;

import com.insights.data.models.Donor;
import com.insights.data.models.DonorStatus;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * This class is responsible for holding the states of all donors in memory.
 */
public class DonorProcessor {
    // a set of donors that are already identified as repeat donors
    private Set<Donor> repeatDonors;
    // a map from the donor to the earliest year where we find a contribution from this donor
    private Map<Donor, Integer> donorToYear;

    public DonorProcessor() {
        this.repeatDonors = new HashSet<>();
        this.donorToYear = new HashMap<>();
    }

    /**
     * fetch the status of the donor given a new contribution in current date to happen
     * perform a side-effect update to this donor while returning the status
     * @param donor the donor we are querying
     * @param currentDate the date of the new contribution we are processing
     * @return
     *  REPEAT                  if donor was identified as repeat before this contribution
     *  NON_REPEAT              if this is the first contribution of the donor, or the year of contribution in record is
     *                          not prior to current year
     *  NON_REPEAT_TO_REPEAT    if donor is identified as repeat with this contribution
     *
     */
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
