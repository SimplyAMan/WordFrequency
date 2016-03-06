package me.model;

import java.io.File;
import java.util.Date;
import java.util.ArrayList;

public class FrequencyModel {
    // Instance field
    private String targetPath;      // target file path
    private String resultBasePath;  // result base path for result.txt and result.jpg
    private String resultFilePath;  // result file path
    private String resultImgPath;   // Column image path
    private int chartColumn = 15;   // Chart column number
    private String typeName = "default"; // Default type name.
    private int wordNumber  = 15;   // Word number to be recorded.
    private int actualNumber;
    private Frequency frequency;    // Frequency
    private FrequencyChart chart;   // Chart
    private TypeStatistic statistic;// TypeStatistic
    private ProcessResult result;   // result, generate the data for Jtable.

    // Constructor
    public FrequencyModel(String targetPath) {
        this.targetPath = targetPath;

        String dir = "" + new Date().getTime();
        new File("./" + "result").mkdirs();
        new File("./result/" + "tmp").mkdirs();
        new File("./result/" + "type").mkdirs();
        new File("./result/tmp/" + dir).mkdirs();

        this.resultBasePath = "./result/tmp/" + dir + "/";
        this.resultFilePath = this.resultBasePath + "result.txt";
        this.resultImgPath  = this.resultBasePath + "result.jpg";

        this.frequency = new Frequency(this.targetPath);
        this.frequency.writeResult(this.resultFilePath); // Now we get the result.txt.

        this.result = new ProcessResult(this.resultFilePath);

        /* Set chartColumn and wordNumber */
        this.actualNumber = this.result.getWordsArray().size();
        if (this.actualNumber < 15) {
            this.chartColumn = this.actualNumber;
            this.wordNumber  = this.actualNumber;
        }

        this.chart = new FrequencyChart(this.resultFilePath, this.chartColumn);
        this.chart.generateImage(this.resultImgPath);    // Now we get the result.jpg.
    }

    /* About the result statisitc */

    // Set the high frequency word number to be recorded.
    public void setWordNumber(int wordNumber) {
        if (wordNumber < this.actualNumber) {
            this.wordNumber = wordNumber;
        } else {
            this.wordNumber = this.actualNumber;
        }
    }

    // Result statistic according to type.
    public void resultStatistic(String typeName) {
        this.statistic = new TypeStatistic(resultFilePath, typeName);
        this.statistic.setWordNumber(wordNumber);
        this.statistic.writeWords();               // Now we get the type file.
    }

    /* About the Jtable's data */
    public ArrayList<String> getWords() {
        return this.result.getWordsArray();
    }

    public ArrayList<Integer> getFrequency() {
        return this.result.getFrequencyArray();
    }

    /* About the chart */
    public String getChartPath() {
        return resultImgPath;
    }


    /** Unit Tests */
    public static void main(String[] args) {
        FrequencyModel model = new FrequencyModel("./test.txt");

        // For statistic.
        model.setWordNumber(20);
        model.resultStatistic("novel");

        // For Jtable
        System.out.println(model.getWords());
        System.out.println(model.getFrequency());

        // For Chart
        System.out.println(model.getChartPath());
    }
}
