package com.insights.data;

import com.insights.data.models.Contribution;
import com.insights.data.processors.ContributionProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {
        String itcont = args[0];
        String line;
        try {
            FileReader fileReader = new FileReader(itcont);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ContributionProcessor contributionProcessor = new ContributionProcessor(30);
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    Contribution contribution = new Contribution(line);
                    System.out.println(contributionProcessor.insertNewContribution(contribution));
                } catch (InputMismatchException ex) {
                    // simply skip invalid input contribution record
                }
            }
        } catch (IOException ex) {
            System.out.println("unable to read or write file ");
        }
    }
}
