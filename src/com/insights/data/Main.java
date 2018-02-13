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
        String itcont = args[0];
        String percentileFile = args[1];
        String outputFile = args[2];
        String line;
        try {
            FileReader percentileReader = new FileReader(percentileFile);
            BufferedReader percentileBuffer = new BufferedReader(percentileReader);
            int percentile = Integer.valueOf(percentileBuffer.readLine());
            percentileReader.close();
            FileReader fileReader = new FileReader(itcont);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            File fout = new File(outputFile);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            ContributionProcessor contributionProcessor = new ContributionProcessor(percentile);

            while ((line = bufferedReader.readLine()) != null) {
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
            fileReader.close();
            bw.close();
        } catch (IOException ex) {
            System.out.println("unable to read or write file ");
        }
    }

}
