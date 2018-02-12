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
        String percentileFile = args[1];
        String line;
        try {
            FileReader percentileReader = new FileReader(percentileFile);
            BufferedReader percentileBuffer = new BufferedReader(percentileReader);
            int percentile = Integer.valueOf(percentileBuffer.readLine());
            percentileReader.close();
            FileReader fileReader = new FileReader(itcont);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ContributionProcessor contributionProcessor = new ContributionProcessor(percentile);
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    Contribution contribution = new Contribution(line);
                    System.out.println(contributionProcessor.insertNewContribution(contribution));
                } catch (InputMismatchException ex) {
                    // simply skip invalid input contribution record
                }
            }
            fileReader.close();
        } catch (IOException ex) {
            System.out.println("unable to read or write file ");
        }
    }
}
