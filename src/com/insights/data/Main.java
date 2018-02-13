package com.insights.data;

import com.insights.data.models.Contribution;
import com.insights.data.processors.ContributionProcessor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {
        String itcontFilePath = args[0];
        String percentileFilePath = args[1];
        String outputFilePath = args[2];

        try {
            FileReader percentileReader = new FileReader(percentileFilePath);
            BufferedReader percentileBuffer = new BufferedReader(percentileReader);
            int percentile = Integer.valueOf(percentileBuffer.readLine());
            percentileReader.close();
            ContributionProcessor contributionProcessor = new ContributionProcessor(percentile);

            FileReader itcontFileReader = new FileReader(itcontFilePath);
            BufferedReader itcontBuffer = new BufferedReader(itcontFileReader);

            File fout = new File(outputFilePath);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            String line;
            while ((line = itcontBuffer.readLine()) != null) {
                try {
                    Contribution contribution = new Contribution(line);
                    String result = contributionProcessor.processNewContribution(contribution);
                    if (result != null) {
                        bw.write(result);
                        bw.newLine();
                    }
                } catch (InputMismatchException ex) {
                    // simply skip invalid input contribution record
                }
            }
            itcontFileReader.close();
            bw.close();
        } catch (IOException ex) {
            System.out.println("unable to read or write file ");
        }
    }

}
