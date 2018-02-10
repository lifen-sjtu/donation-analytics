package com.insights.data.models;

/**
 * Created by Fen Li on 2/9/18.
 */
public class Donor {
    private String name;
    private String zipCode;
    public Donor(String name, String zipCode) {
        this.name = name;
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Donor) {
            Donor anotherDonor = (Donor) obj;
            return name.equals(anotherDonor.getName()) && zipCode.equals(anotherDonor.getZipCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + zipCode.hashCode();
    }
}
