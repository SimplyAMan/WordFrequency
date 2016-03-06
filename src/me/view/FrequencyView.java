package me.view;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JFrame;

public class FrequencyView extends JFrame {
    // Frame Size
    private static final int DEFAULT_WIDTH  = 800;
    private static final int DEFAULT_HEIGHT = 600;

    // Menu
    private JMenuBar  menuBar  = new JMenuBar();
    private JMenu     menu     = new JMenu("文件");
    private JMenuItem openItem = new JMenuItem("打开");
    private JMenuItem typeItem = new JMenuItem("类型");
    private JMenuItem exitItem = new JMenuItem("退出");

    // Panel
    private JPanel content = new JPanel();

    private ImageComponent chart = new ImageComponent();
    private JScrollPane chartPane = new JScrollPane(chart);

    // Table, NOTICE: The sequence is necessary. model->table->tablePane.
    private DefaultTableModel model = new DefaultTableModel();
    private JTable table = new JTable(model) { public boolean isCellEditable(int arg0, int arg1) { return false; }};;
    private JScrollPane tablePane = new JScrollPane(table);

    /** Constructor */
    public FrequencyView() {
        // Set up the menu
        this.setJMenuBar(menuBar);
        this.menuBar.add(menu); this.menu.add(openItem); this.menu.add(typeItem);
        this.menu.add(exitItem);

        // Chart and Tabel
        JPanel p = new JPanel(new BorderLayout());
        this.chartPane.setPreferredSize(new Dimension(600, 600));
        this.tablePane.setPreferredSize(new Dimension(200, 600));
        p.add(chartPane, BorderLayout.WEST);
        p.add(tablePane, BorderLayout.EAST);
        this.content.setLayout(new BorderLayout());
        this.content.add(p, BorderLayout.CENTER);

        // Finalize layout.
        this.setContentPane(content);
        this.setVisible(true);
        this.setTitle("Word Frequency");
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JMenuItem getOpenItem() {
        return this.openItem;
    }

    /** Some Actions */
    public void setTheChart(String path) {
        this.chart.setImage(path);
        this.chart.repaint(); // repaint is necessary.
    }

    public void setTheTable(ArrayList<String> words, ArrayList<Integer> frequency) {
        String[] wo = words.toArray(new String[words.size()]);
        Integer[] fr = frequency.toArray(new Integer[frequency.size()]);

        this.model.addColumn("Words", wo);
        this.model.addColumn("Frequency", fr);
        this.table.setModel(this.model);
        this.table.repaint(); // repaint is necessary.
    }

    /** Add Listener */
    public void addOpenListener(ActionListener open) {
        openItem.addActionListener(open);
    }

    public void addTypeListener(ActionListener type) {
        typeItem.addActionListener(type);
    }

    public void addExitListener(ActionListener exit) {
        exitItem.addActionListener(exit);
    }

    // Inner class to display the chart.
    class ImageComponent extends JComponent {
        private static final int WIDTH = 600;
        private static final int HEIGHT = 600;
        private Image image;

        public ImageComponent() {
            image = new ImageIcon().getImage();
        }

        public void setImage(String path) {
            image = new ImageIcon(path).getImage();
        }

        public void paintComponent(Graphics g) {
            if (image == null) return;

            int imageWidth = image.getWidth(this);
            int imageHeight = image.getHeight(this);

            g.drawImage(image, 0, 0, null);
        }

        public Dimension getPreferredSize() {
            return new Dimension(WIDTH, HEIGHT);
        }
    }

    /* Unit Test */
    public static void main(String[] args) {
        ArrayList<String> wo = new ArrayList<String>();
        ArrayList<Integer> fr = new ArrayList<Integer>();
        wo.add("apple"); wo.add("banana"); wo.add("orange");
        fr.add(1); fr.add(2); fr.add(3);

        FrequencyView frame = new FrequencyView();
//        frame.setTheChart("default.jpg"); // It works!!
        frame.setTheTable(wo, fr);
    }
}
