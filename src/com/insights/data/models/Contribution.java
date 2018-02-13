package com.insights.data.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;

/**
 * data model of a contribution. responsible for converting a string record to the contribution entity.
 */
public class Contribution {
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");
    private String recipient;
    private Donor donor;
    private LocalDate transactionDate;
    private float transactionAmount;

    public Contribution(String record) throws InputMismatchException {
        if (record == null) {
            throw new InputMismatchException("input record is null");
        }
        String[] data = record.split("\\|");
        if (data.length != 21) {
            throw new InputMismatchException("could not recognize the input format");
        }

        // validate recipient
        if (data[0].isEmpty()) {
            throw new InputMismatchException("empty cmte_id");
        }
        this.recipient = data[0];

        // validate donor name
        if (data[7].isEmpty()) {
            throw new InputMismatchException("empty name");
        }

        // validate zip code
        if (data[10].length() < 5) {
            throw new InputMismatchException("invalid zipcode");
        }

        this.donor = new Donor(data[7], data[10].substring(0, 5));

        // validate transaction date
        this.transactionDate = LocalDate.parse(data[13], DATE_FORMATTER);
        if (this.transactionDate == null) {
            throw new InputMismatchException("transaction date is invalid");
        }

        // validate transaction amount
        try {
            this.transactionAmount = Float.valueOf(data[14]);
        } catch (NumberFormatException ex) {
            throw new InputMismatchException("could not recognize the input format");
        }

        // validate individual contribution
        if (!data[15].isEmpty()) {
            throw new InputMismatchException("not individual contribution");
        }
    }

    // visible for testing purpose only
    public Contribution(float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    // visible for testing purpose only
    public Contribution(int recipientId) {
        this.recipient = String.valueOf(recipientId);
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public float getTransactionAmount() {
        return transactionAmount;
    }

    public Donor getDonor() {
        return donor;
    }

    public String getRecipient() {
        return recipient;
    }

}
