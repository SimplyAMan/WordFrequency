package me.model;

import java.util.*;
import java.awt.Font;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileOutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class FrequencyChart {
    private int colNumber;
    private String resultPath;

    /**
     * Constructor
     * @param filePath the path of text file.
     * @param colNumber the number of column on the chart.
     */
    public FrequencyChart(String resultPath, int colNumber) {
        this.resultPath  = resultPath;
        this.colNumber   = colNumber;
    }

    /** Build data set */
    private CategoryDataset getDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        ProcessResult words = new ProcessResult(resultPath, colNumber);
        SortedSet<Map.Entry<String, Integer>> wordsSet = words.getResultSet();

        // iterate
        Iterator<Map.Entry<String, Integer>> it = wordsSet.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> m = it.next();
            dataset.addValue(m.getValue(), "AA", m.getKey());
        }

        return dataset;
    }

    /**
     * createChart
     * @param dataset CategoryDataset
     */
    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart3D(
            "词频统计柱状图",
            "单词",
            "数量",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );

        chart.setTitle(new TextTitle("词频统计柱状图", new Font("宋体", Font.BOLD, 25)));

        // CategoryAxis properties
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis categoryAxis = (CategoryAxis) plot.getDomainAxis();
        categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 15));
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 15));

        // NumberAxis properties
        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 15));

        return chart;
    }


    /** generate a chart picture. */
    public void generateImage(String imagePath) {
        try {
            FileOutputStream fos = new FileOutputStream(imagePath);
            ChartUtilities.writeChartAsJPEG(fos, 1, createChart(getDataset()), 600, 525, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /** Unit Test */
    public static void main(String[] args) {
        // ATENTION the path when testing.
        FrequencyChart chart = new FrequencyChart("./result.txt", 15);
        chart.generateImage("./test.jpg");
    }
}
