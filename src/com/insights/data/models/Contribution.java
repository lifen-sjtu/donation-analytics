package com.insights.data.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;

/**
 * Created by Fen Li on 2/9/18.
 */
public class Contribution {
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMddyyyy");
    private String recipient;
    private Donor donor;
    private Date transactionDate;
    private float transactionAmount;

    public Contribution(String record) throws InputMismatchException {
        if (record == null) {
            throw new InputMismatchException("input record is null");
        }
        String[] data = record.split("\\|");
        if (data == null || data.length != 21) {
            throw new InputMismatchException("could not recognize the input format");
        }
        if (data[10].length() < 5) {
            throw new InputMismatchException("invalid zipcode");
        }
        if (!data[15].isEmpty()) {
            throw new InputMismatchException("not individual contribution");
        }
        this.recipient = data[0];
        this.donor = new Donor(data[7], data[10].substring(0, 5));
        try {
            this.transactionDate = DATE_FORMAT.parse(data[13]);
            this.transactionAmount = Float.valueOf(data[14]);
        } catch (ParseException | NumberFormatException ex) {
            throw new InputMismatchException("could not recognize the input format");
        }
    }

    public Date getTransactionDate() {
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
