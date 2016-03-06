package me.controller;

import me.view.*;
import me.model.*;

import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FrequencyControl {
    private FrequencyModel model;
    private FrequencyView  view = new FrequencyView();

    public FrequencyControl() {
        view.addOpenListener(new OpenListener());
        view.addTypeListener(new TypeListener());
        view.addExitListener(new ExitListener());
    }

    // Inner class MultiplyListener
    class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("文本文件", "txt");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(new File("."));

            int flag = chooser.showOpenDialog(view.getOpenItem());
            if (flag == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getPath();
                model = new FrequencyModel(path);
                view.setTheChart(model.getChartPath());
                view.setTheTable(model.getWords(), model.getFrequency());
            }
        }
    }

    class TypeListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (model != null) {
                String fileType = JOptionPane.showInputDialog("请输入文章类型: ");
                String wordNumber = JOptionPane.showInputDialog("请输入统计单词数: ");
                int number = 0;
                try {
                    number = Integer.parseInt(wordNumber);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "请输入整数!");
                }

                if (number != 0) {
                    model.setWordNumber(number);
                    model.resultStatistic(fileType);
                    JOptionPane.showMessageDialog(null, "统计完成, 文件位于./result/type/");
                }
            }
        }
    }

    class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }
}
